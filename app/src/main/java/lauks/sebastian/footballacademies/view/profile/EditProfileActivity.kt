package lauks.sebastian.footballacademies.view.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_edit_profile.*
import lauks.sebastian.footballacademies.R
import lauks.sebastian.footballacademies.model.Player
import lauks.sebastian.footballacademies.utilities.InjectorUtils
import lauks.sebastian.footballacademies.viewmodel.profile.EditProfileViewModel

class EditProfileActivity : AppCompatActivity() {
    private lateinit var viewModel:EditProfileViewModel
    private lateinit var loggedUserId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val factory = InjectorUtils.provideEditProfileViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(EditProfileViewModel::class.java)

        loggedUserId = intent.getStringExtra("loggedUserId")!!
        setupSpinner()
        setupButtons()

showRefreshingIndicator()
        viewModel.fetchUser(loggedUserId, updateFields)
        }

    private val updateFields = {player: Player ->
        et_edit_profile_firstname.setText(player.firstname)
        et_edit_profile_lastname.setText(player.lastname)

        if(player.age != null) et_edit_profile_age.setText(player.age.toString()) else et_edit_profile_age.setText("")
        if(player.height != null) et_edit_profile_height.setText(player.height.toString()) else et_edit_profile_height.setText("")
        if(player.weight != null) et_edit_profile_weight.setText(player.weight.toString()) else et_edit_profile_weight.setText("")

        spinner_edit_profile_pref_foot.setSelection(player.prefFoot ?: 0)

        hideRefreshingIndicator()

    }

    private fun showRefreshingIndicator(){
        progressBar1.visibility = View.VISIBLE
        linear_layout_content.visibility = View.GONE
    }

    private fun hideRefreshingIndicator(){
        progressBar1.visibility = View.GONE
        linear_layout_content.visibility = View.VISIBLE
    }

    private fun setupButtons(){

        bt_edit_profile_cancel.setOnClickListener {
            finish()
        }

        val finishSetting = {
            hideRefreshingIndicator()
            finish()
        }

        bt_edit_profile_apply.setOnClickListener {
            val firstname = et_edit_profile_firstname.text.toString()
            val lastname = et_edit_profile_lastname.text.toString()
            val age = if (et_edit_profile_age.text.toString()!= "") et_edit_profile_age.text.toString().toInt() else null
            val height = if (et_edit_profile_height.text.toString() != "") et_edit_profile_height.text.toString().toInt() else null
            val weight = if (et_edit_profile_weight.text.toString() != "") et_edit_profile_weight.text.toString().toInt() else null
            val prefFoot = spinner_edit_profile_pref_foot.selectedItemPosition
            viewModel.setUserDetails(loggedUserId, firstname, lastname, height,weight,age,prefFoot, finishSetting)

        }
    }

    private fun setupSpinner(){
        val spinner = findViewById<Spinner>(R.id.spinner_edit_profile_pref_foot)
        val adapter = ArrayAdapter.createFromResource(this, R.array.pref_feet_array, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }
}
