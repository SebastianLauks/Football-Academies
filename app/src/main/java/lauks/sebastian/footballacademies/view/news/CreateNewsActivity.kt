package lauks.sebastian.footballacademies.view.news

import android.app.ActionBar
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_create_news.*
import lauks.sebastian.footballacademies.R
import lauks.sebastian.footballacademies.utilities.InjectorUtils
import lauks.sebastian.footballacademies.viewmodel.news.NewsViewModel
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.util.*

class CreateNewsActivity : AppCompatActivity() {
    private lateinit var viewModel: NewsViewModel
    private lateinit var loggedUserId: String
    private val PHOTO_STATUS_CODE = 111
    private var bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_news)

        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)

        val factory = InjectorUtils.provideNewsViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(NewsViewModel::class.java)
        val sharedPref =
            getSharedPreferences(resources.getString(R.string.app_name), Context.MODE_PRIVATE)
        loggedUserId = sharedPref.getString("loggedUserId", "unknown").toString()

        bt_news_cancel.setOnClickListener {
            finish()
        }

        bt_news_add.setOnClickListener {
            when {
                et_news_title.text.isEmpty() -> Toast.makeText(
                    applicationContext,
                    R.string.create_post_empty_title,
                    Toast.LENGTH_SHORT
                ).show()
                et_news_content.text.isEmpty() -> Toast.makeText(
                    applicationContext,
                    R.string.create_post_empty_content,
                    Toast.LENGTH_SHORT
                ).show()
                else -> {
                    var imageName: String? = null
                    var imageUrl: String? = null
                    val localBitmap = bitmap!!
                    val baos = ByteArrayOutputStream()
                    localBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val data = baos.toByteArray()
                    progressBar1.visibility = View.VISIBLE
                    viewModel.uploadImage(UUID.randomUUID().toString(), data) { success: Boolean, name: String, fileUrl: String ->
                        if (true) {
                            imageName = name
                            imageUrl = fileUrl
                        }
                        progressBar1.visibility = View.GONE
                        viewModel.addNews(
                            loggedUserId,
                            et_news_title.text.toString(),
                            et_news_content.text.toString(),
                            Date(),
                            imageName,
                            imageUrl
                        )

                        et_news_content.setText("")
                        et_news_title.setText("")
                        Toast.makeText(
                            applicationContext,
                            R.string.create_post_added,
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }



                }
            }
        }

        bt_add_image.setOnClickListener {
            val photoIntent = Intent(Intent.ACTION_GET_CONTENT)
            photoIntent.type = "image/*"
            startActivityForResult(photoIntent, PHOTO_STATUS_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PHOTO_STATUS_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val chosenImageUri = data!!.data
                try {
                    chosenImageUri.let {
                        if (Build.VERSION.SDK_INT < 28) {
                            bitmap =
                                MediaStore.Images.Media.getBitmap(contentResolver, chosenImageUri)
                            image_news.setImageBitmap(bitmap)
                        } else {
                            val source =
                                ImageDecoder.createSource(contentResolver, chosenImageUri!!)
                            bitmap = ImageDecoder.decodeBitmap(source)
                            image_news.setImageBitmap(bitmap)
                        }
                        image_news.visibility = View.VISIBLE

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
