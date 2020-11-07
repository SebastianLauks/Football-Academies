package lauks.sebastian.footballacademies.view.news

import android.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_create_news.*
import lauks.sebastian.footballacademies.R

class CreateNewsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_news)

        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)


        bt_news_cancel.setOnClickListener {
            finish()
        }

        bt_news_add.setOnClickListener {
            if(et_news_title.text.isEmpty()){
                Toast.makeText(applicationContext, R.string.create_post_empty_title,Toast.LENGTH_SHORT).show()
            }else if (et_news_content.text.isEmpty()){
                Toast.makeText(applicationContext, R.string.create_post_empty_content,Toast.LENGTH_SHORT).show()
            }else{
                //HERE WILL BE ADDING NEW POST LOGIC
                et_news_content.setText("")
                et_news_title.setText("")
                Toast.makeText(applicationContext, R.string.create_post_added,Toast.LENGTH_SHORT).show()
                finish()
            }

        }
    }
}
