package lauks.sebastian.footballacademies.view.drawer

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_info.*
import lauks.sebastian.footballacademies.R
import lauks.sebastian.footballacademies.utilities.InjectorUtils
import lauks.sebastian.footballacademies.viewmodel.academies.AcademiesViewModel

class InfoActivity : AppCompatActivity() {
    private lateinit var viewModel: AcademiesViewModel
    private lateinit var chosenAcademyId:String
    private lateinit var academyCode: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val factory = InjectorUtils.provideAcademiesViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(AcademiesViewModel::class.java)

        supportActionBar?.setDisplayHomeAsUpEnabled(true) // back arrow

        chosenAcademyId = intent.extras!!.get("chosenAcademyId").toString()
        showLoading()
        viewModel.fetchAcademy(chosenAcademyId){
            hideLoading()
            academyCode = it.code
        }


        switch1.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                tv_info_code.text = academyCode
            }else{
                tv_info_code.text = "**********"
            }
        }

        tv_info_code.setOnClickListener {
            if(switch1.isChecked){
                Toast.makeText(this, "Przytrzymaj dłużej, aby skopiować kod.", Toast.LENGTH_SHORT).show()
            }
        }

        tv_info_code.setOnLongClickListener {
            if(switch1.isChecked){
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                var clip = ClipData.newPlainText("kod", tv_info_code.text.toString())
                clipboard.setPrimaryClip(clip)
                Toast.makeText(this, "Kod został skopiowany", Toast.LENGTH_SHORT).show()
            }
            true
        }


    }

    //back arrow on click
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showLoading(){
        progressBar1.visibility = View.VISIBLE
        layout_content.visibility = View.GONE
    }

    private fun hideLoading(){
        progressBar1.visibility = View.GONE
        layout_content.visibility = View.VISIBLE
    }
}
