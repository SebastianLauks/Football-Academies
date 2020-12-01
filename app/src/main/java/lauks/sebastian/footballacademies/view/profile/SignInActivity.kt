package lauks.sebastian.footballacademies.view.profile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_sign_in.*
import lauks.sebastian.footballacademies.R
import lauks.sebastian.footballacademies.utilities.InjectorUtils
import lauks.sebastian.footballacademies.view.academies.AcademiesActivity
import lauks.sebastian.footballacademies.viewmodel.profile.ProfileViewModel

class SignInActivity : AppCompatActivity() {
    private lateinit var viewModel: ProfileViewModel
    private lateinit var login: String
    private lateinit var password: String
    private lateinit var loadingIndicator: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val factory = InjectorUtils.provideProfileViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(ProfileViewModel::class.java)
        loadingIndicator = findViewById(R.id.progressBar1)

        checkIfUsersIsAlreadyLogged()

        bt_sign_in.setOnClickListener {
            loadingIndicator.visibility = View.VISIBLE
            login = et_login.text.toString()
            password = et_password.text.toString()

            viewModel.checkCredentials(login, password, successfulSignIn)
        }

        bt_sign_up_ref.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

    }

    private val successfulSignIn = {success:Boolean ->
        loadingIndicator.visibility = View.GONE
        if (success){
            val sharedPref = getSharedPreferences( resources.getString(R.string.app_name),Context.MODE_PRIVATE)
            with (sharedPref.edit()){
                putString("loggedUserId", login)
                commit()
            }
            val intent = Intent(this, AcademiesActivity::class.java)
            startActivity(intent)
        }else{
            et_password.setText("")
            Toast.makeText(this, "Niepoprawna nazwa użytkownika lub hasło", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkIfUsersIsAlreadyLogged(){
        layout_content.visibility = View.GONE
        loadingIndicator.visibility = View.VISIBLE
        val sharedPref = getSharedPreferences( resources.getString(R.string.app_name),Context.MODE_PRIVATE)
        val userId = sharedPref.getString("loggedUserId", "unknown").toString()
        if(userId !="unknown" ){
            val intent = Intent(this, AcademiesActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "Zalogowano jako $userId", Toast.LENGTH_SHORT).show()
        }else{
            layout_content.visibility = View.VISIBLE
            loadingIndicator.visibility = View.GONE
        }
    }

}
