package lauks.sebastian.footballacademies.view.academies

import android.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_create_academy.*
import lauks.sebastian.footballacademies.R
import lauks.sebastian.footballacademies.utilities.InjectorUtils
import lauks.sebastian.footballacademies.viewmodel.academies.AcademiesViewModel

class CreateAcademyActivity : AppCompatActivity() {

    private lateinit var viewModel: AcademiesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_academy)


        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)

        val factory = InjectorUtils.provideAcademiesViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(AcademiesViewModel::class.java)



        bt_academies_cancel.setOnClickListener {
            finish()
        }

        bt_academies_add.setOnClickListener {
            when{
                et_academies_name.text.isEmpty() -> Toast.makeText(applicationContext, R.string.create_academy_empty_name,Toast.LENGTH_SHORT).show()
                else -> {
                    val loggedUserId = "user0001" // ToDo HERE FROM SHARE PREF
                    viewModel.addAcademy(et_academies_name.text.toString())
                    et_academies_name.setText("")
                    Toast.makeText(applicationContext, R.string.create_academy_added,Toast.LENGTH_SHORT).show()
                    finish()
                }

            }
        }
    }
}
