package lauks.sebastian.footballacademies.view.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.news_item.view.*
import lauks.sebastian.footballacademies.R
import lauks.sebastian.footballacademies.model.User
import lauks.sebastian.footballacademies.model.news.News
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class NewsAdapter (private val newsList: LiveData<List<News>>, private val usersList: LiveData<List<User>>): RecyclerView.Adapter<NewsAdapter.NewsViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        val holder = NewsViewHolder(itemView)

        //HERE e.g. holder.itemView.setOnClickListener{....

        return holder
    }

    override fun getItemCount() = newsList.value!!.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = newsList.value!![position]
        val usersList = usersList.value!!
        holder.tvFirstName.text = usersList.find { user -> user.id == currentItem.authorId }?.firstname
        holder.tvLastName.text = usersList.find { user -> user.id == currentItem.authorId }?.lastname
        holder.tvTitle.text = currentItem.title
        holder.tvContent.text = currentItem.content
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
        holder.tvCreationDate.text = formatter.format(Date(currentItem.creationDate))
    }


    class NewsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvFirstName = itemView.tv_post_firstname
        val tvLastName = itemView.tv_post_lastname
        val tvTitle = itemView.tv_post_title
        val tvContent = itemView.tv_post_content
        val tvCreationDate = itemView.tv_post_date
    }
}