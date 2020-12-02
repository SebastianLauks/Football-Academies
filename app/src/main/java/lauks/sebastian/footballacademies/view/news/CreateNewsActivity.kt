package lauks.sebastian.footballacademies.view.news

import android.app.ActionBar
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.MediaController
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
    private var videoUri: Uri? = null

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
                et_news_title.text.length > 100 -> Toast.makeText(
                    applicationContext,
                    "Tytuł postu nie może być dłuższy niż 100 znaków",
                    Toast.LENGTH_SHORT
                ).show()
                else -> {
                    progress_layout.visibility = View.VISIBLE
                    bt_news_add.isClickable = false
                    bt_news_cancel.isClickable = false
                    var imageName: String? = null
                    var imageUrl: String? = null
                    var videoName: String? = null
                    var videoUrl: String? = null
                    val localBitmap = bitmap
                    val baos = ByteArrayOutputStream()
                    localBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    var data: ByteArray? = baos.toByteArray()
                    if (bitmap != null) {
                        viewModel.uploadImage(
                            UUID.randomUUID().toString(),
                            data
                        ) { success: Boolean, name: String, fileUrl: String ->
                            if (true) {
                                imageName = name
                                imageUrl = fileUrl
                            }
                            addNewPost(imageName, imageUrl, videoName, videoUrl)
                        }
                    } else if (videoUri != null) {
                        viewModel.uploadVideo(
                            UUID.randomUUID().toString(),
                            videoUri!!,
                            progressCallback
                        ) { success, name, fileUrl ->
                            if (true) {
                                videoName = name
                                videoUrl = fileUrl
                            }
                            addNewPost(imageName, imageUrl, videoName, videoUrl)
                        }
                    } else {
                        addNewPost(imageName, imageUrl, videoName, videoUrl)
                    }


                }
            }
        }

        bt_add_image.setOnClickListener {
            val photoIntent = Intent(Intent.ACTION_PICK)
            photoIntent.type = "image/* video/*"
            startActivityForResult(photoIntent, PHOTO_STATUS_CODE)
        }
    }


    private val progressCallback = {progress:Double ->
        progressBar1.progress = progress.toInt()
        val text = "${progress.toInt()}%"
        tv_progress_value.text = text
    }
    private fun addNewPost(
        imageName: String?,
        imageUrl: String?,
        videoName: String?,
        videoUrl: String?
    ) {
        progress_layout.visibility = View.GONE
        viewModel.addNews(
            loggedUserId,
            et_news_title.text.toString(),
            et_news_content.text.toString(),
            Date(),
            imageName,
            imageUrl,
            videoName,
            videoUrl
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        video_news.visibility = View.GONE
        image_news.visibility = View.GONE
        if (requestCode == PHOTO_STATUS_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val chosenUri = data!!.data
                if (chosenUri.toString().contains("image")) {
                    try {
                        chosenUri.let {
                            if (Build.VERSION.SDK_INT < 28) {
                                bitmap =
                                    MediaStore.Images.Media.getBitmap(contentResolver, chosenUri)
                                image_news.setImageBitmap(bitmap)
                            } else {
                                val source =
                                    ImageDecoder.createSource(contentResolver, chosenUri!!)
                                bitmap = ImageDecoder.decodeBitmap(source)
                                image_news.setImageBitmap(bitmap)
                            }
                            image_news.visibility = View.VISIBLE

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else if (chosenUri.toString().contains("video")) {
                    val mediaController = MediaController(this)
                    video_news.visibility = View.VISIBLE
                    mediaController.setAnchorView(video_news)
                    video_news.setMediaController(mediaController)
                    video_news.setVideoURI(chosenUri)
                    video_news.requestFocus()
                    videoUri = chosenUri
                }
            }
        }
    }
}
