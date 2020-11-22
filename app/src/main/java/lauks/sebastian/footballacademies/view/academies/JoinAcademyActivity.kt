package lauks.sebastian.footballacademies.view.academies

import android.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_create_academy.*
import kotlinx.android.synthetic.main.activity_create_academy.bt_academies_cancel
import kotlinx.android.synthetic.main.activity_join_academy.*
import lauks.sebastian.footballacademies.R
import lauks.sebastian.footballacademies.utilities.InjectorUtils
import lauks.sebastian.footballacademies.viewmodel.academies.AcademiesViewModel

class JoinAcademyActivity : AppCompatActivity() {

    private lateinit var viewModel: AcademiesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_academy)

        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)

        val factory = InjectorUtils.provideAcademiesViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(AcademiesViewModel::class.java)



        bt_join_academies_cancel.setOnClickListener {
            finish()
        }

        bt_join_academies_join.setOnClickListener {
            when {
                et_join_academies_code.text.isEmpty() -> Toast.makeText(
                    applicationContext, R.string.join_academy_empty_code,
                    Toast.LENGTH_SHORT
                ).show()
                else -> {
                    val loggedUserId = "user0001" // ToDo HERE FROM SHARE PREF
                    viewModel.addToSquad(et_join_academies_code.text.toString())
                    et_join_academies_code.setText("")
                    Toast.makeText(
                        applicationContext,
                        R.string.join_academy_joined,
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }

            }
        }
    }
}
