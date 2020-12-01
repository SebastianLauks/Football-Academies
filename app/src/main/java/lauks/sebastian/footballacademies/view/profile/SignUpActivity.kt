package lauks.sebastian.footballacademies.view.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.et_login
import kotlinx.android.synthetic.main.activity_sign_up.et_password
import lauks.sebastian.footballacademies.R
import lauks.sebastian.footballacademies.utilities.InjectorUtils
import lauks.sebastian.footballacademies.viewmodel.profile.ProfileViewModel

class SignUpActivity : AppCompatActivity() {
    private lateinit var viewModel: ProfileViewModel
    private lateinit var login: String
    private lateinit var password: String
    private lateinit var passwordRepeat: String
    private lateinit var loadingIndicator: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val factory = InjectorUtils.provideProfileViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(ProfileViewModel::class.java)

        loadingIndicator = findViewById(R.id.progressBar1)

        bt_sign_up.setOnClickListener {
            loadingIndicator.visibility = View.VISIBLE
            login = et_login.text.toString()
            password = et_password.text.toString()
            passwordRepeat = et_password_repeat.text.toString()

            if (password != passwordRepeat){
                Toast.makeText(this, "Wprowadzone hasła się różnią", Toast.LENGTH_SHORT).show()
                loadingIndicator.visibility = View.GONE
            }else{

                viewModel.signUpUser(login, password, signUpCallback)
            }
        }
        bt_sign_in_ref.setOnClickListener {
            finish()
        }

    }

    private val signUpCallback = {success: Boolean, message: String ->
        if(success){
            et_login.setText("")
            et_password.setText("")
            et_password_repeat.setText("")
            Toast.makeText(this ,"Rejestracja przebiegła pomyślnie. Proszę się zalogować", Toast.LENGTH_LONG).show()
            finish()
        }else{
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
        loadingIndicator.visibility = View.GONE
    }
}
