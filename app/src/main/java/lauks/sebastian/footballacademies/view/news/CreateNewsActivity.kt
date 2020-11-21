package lauks.sebastian.footballacademies.view.news

import android.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_create_news.*
import lauks.sebastian.footballacademies.R
import lauks.sebastian.footballacademies.utilities.InjectorUtils
import lauks.sebastian.footballacademies.viewmodel.news.NewsViewModel
import java.util.*

class CreateNewsActivity : AppCompatActivity() {
    private lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_news)

        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)

        val factory = InjectorUtils.provideNewsViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(NewsViewModel::class.java)


        bt_news_cancel.setOnClickListener {
            finish()
        }

        bt_news_add.setOnClickListener {
            when {
                et_news_title.text.isEmpty() -> Toast.makeText(applicationContext, R.string.create_post_empty_title,Toast.LENGTH_SHORT).show()
                et_news_content.text.isEmpty() -> Toast.makeText(applicationContext, R.string.create_post_empty_content,Toast.LENGTH_SHORT).show()
                else -> {
                    val loggedUserId = "user0001" // ToDo HERE FROM SHARE PREF
                    viewModel.addNews(loggedUserId, et_news_title.text.toString(), et_news_content.text.toString(), Date())

                    et_news_content.setText("")
                    et_news_title.setText("")
                    Toast.makeText(applicationContext, R.string.create_post_added,Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

        }
    }
}
