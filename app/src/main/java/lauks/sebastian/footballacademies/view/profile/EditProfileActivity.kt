package lauks.sebastian.footballacademies.view.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_create_news.*
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.progressBar1
import lauks.sebastian.footballacademies.R
import lauks.sebastian.footballacademies.model.Player
import lauks.sebastian.footballacademies.utilities.InjectorUtils
import lauks.sebastian.footballacademies.viewmodel.profile.ProfileViewModel
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.util.*

class EditProfileActivity : AppCompatActivity() {
    private lateinit var viewModel:ProfileViewModel
    private lateinit var loggedUserId: String
    private val PHOTO_STATUS_CODE = 100
    private var bitmap: Bitmap? = null
    private var bitmapFromDB: Bitmap? = null
    private var player:Player? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val factory = InjectorUtils.provideProfileViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(ProfileViewModel::class.java)

        loggedUserId = intent.getStringExtra("loggedUserId")!!
        setupSpinner()
        setupSpinnerRole()
        setupButtons()

showRefreshingIndicator()
        viewModel.fetchUser(loggedUserId, updateFields)
        }

    private val updateFields = {player: Player ->
        et_edit_profile_firstname.setText(player.firstname)
        et_edit_profile_lastname.setText(player.lastname)

        this.player = player

        if(player.age != null) et_edit_profile_age.setText(player.age.toString()) else et_edit_profile_age.setText("")
        if(player.height != null) et_edit_profile_height.setText(player.height.toString()) else et_edit_profile_height.setText("")
        if(player.weight != null) et_edit_profile_weight.setText(player.weight.toString()) else et_edit_profile_weight.setText("")

        spinner_edit_profile_pref_foot.setSelection(player.prefFoot ?: 0)

        spinner_role.setSelection(player.role ?: 0)

        showRefreshingIndicator()
        if(player.imageName != null && player.imageName != ""){
            Picasso.get().load(player.imageUrl).into(iv_profile_image, object : com.squareup.picasso.Callback {
                override fun onSuccess() {
                    bitmapFromDB = (iv_profile_image.drawable as BitmapDrawable).bitmap
                    hideRefreshingIndicator()
                }

                override fun onError(e: java.lang.Exception?) {
                    hideRefreshingIndicator()

                }
            })
        }


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

        bt_edit_profile_image.setOnClickListener {
            val photoIntent = Intent(Intent.ACTION_PICK)
            photoIntent.type = "image/*"
            startActivityForResult(photoIntent, PHOTO_STATUS_CODE)
        }

        bt_edit_profile_apply.setOnClickListener {
            val firstname = et_edit_profile_firstname.text.toString()
            val lastname = et_edit_profile_lastname.text.toString()
            val age = if (et_edit_profile_age?.text.toString()!= "") et_edit_profile_age?.text.toString().toInt() else null
            val height = if (et_edit_profile_height?.text.toString() != "") et_edit_profile_height?.text.toString().toInt() else null
            val weight = if (et_edit_profile_weight?.text.toString() != "") et_edit_profile_weight?.text.toString().toInt() else null
            val prefFoot = spinner_edit_profile_pref_foot.selectedItemPosition
            val role = spinner_role.selectedItemPosition

            val imageName = player?.imageName
            val imageUrl = player?.imageUrl
            showRefreshingIndicator()
            if(bitmap != bitmapFromDB && bitmap != null){
                val localBitmap = bitmap
                val baos = ByteArrayOutputStream()
                localBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                var data: ByteArray? = baos.toByteArray()
                viewModel.uploadImage(UUID.randomUUID().toString(),data){success, name, fileUrl ->
                    if(success){
                        if(player?.imageName != null)
                            viewModel.removeImage(player?.imageName!!){
                        }

                        viewModel.setUserDetails(loggedUserId, firstname, lastname, height,weight,age,prefFoot, role, name, fileUrl,finishSetting)
                    }else{
                        hideRefreshingIndicator()
                    }
                }
            }else{
                viewModel.setUserDetails(loggedUserId, firstname, lastname, height,weight,age,prefFoot,role,imageName, imageUrl, finishSetting)

            }


        }
    }

    private fun setupSpinner(){
        val spinner = findViewById<Spinner>(R.id.spinner_edit_profile_pref_foot)
        val adapter = ArrayAdapter.createFromResource(this, R.array.pref_feet_array, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun setupSpinnerRole(){
        val spinner = findViewById<Spinner>(R.id.spinner_role)
        val adapter = ArrayAdapter.createFromResource(this, R.array.user_roles, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                et_edit_profile_height.visibility = View.VISIBLE
                et_edit_profile_weight.visibility = View.VISIBLE
                spinner_edit_profile_pref_foot.visibility = View.VISIBLE
                et_edit_profile_age.visibility = View.VISIBLE
                tv_edit_profile_height.visibility = View.VISIBLE
                tv_edit_profile_weight.visibility = View.VISIBLE
                tv_edit_profile_pref_foot.visibility = View.VISIBLE
                tv_edit_profile_age.visibility = View.VISIBLE
                if(position != 1){
                    tv_edit_profile_height.visibility = View.GONE
                    tv_edit_profile_weight.visibility = View.GONE
                    tv_edit_profile_pref_foot.visibility = View.GONE
                    et_edit_profile_age.visibility = View.GONE
                    et_edit_profile_height.visibility = View.GONE
                    et_edit_profile_weight.visibility = View.GONE
                    spinner_edit_profile_pref_foot.visibility = View.GONE
                    tv_edit_profile_age.visibility = View.GONE
                }
                    }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PHOTO_STATUS_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val chosenUri = data!!.data
                try {
                    chosenUri.let {
                        if (Build.VERSION.SDK_INT < 28) {
                            @Suppress("DEPRECATION")
                            bitmap =
                                MediaStore.Images.Media.getBitmap(contentResolver, chosenUri)
                            iv_profile_image.setImageBitmap(bitmap)
                        } else {
                            val source =
                                ImageDecoder.createSource(contentResolver, chosenUri!!)
                            bitmap = ImageDecoder.decodeBitmap(source)
                            iv_profile_image.setImageBitmap(bitmap)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
