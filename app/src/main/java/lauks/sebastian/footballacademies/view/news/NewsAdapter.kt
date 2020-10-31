package lauks.sebastian.footballacademies.view.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.news_item.view.*
import lauks.sebastian.footballacademies.R
import lauks.sebastian.footballacademies.model.news.News

class NewsAdapter (private val newsList: LiveData<List<News>>): RecyclerView.Adapter<NewsAdapter.NewsViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        val holder = NewsViewHolder(itemView)

        //HERE e.g. holder.itemView.setOnClickListener{....

        return holder
    }

    override fun getItemCount() = newsList.value!!.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = newsList.value!![position]
        holder.tvFirstName.text = currentItem.author.firstname
        holder.tvLastName.text = currentItem.author.lastname
        holder.tvTitle.text = currentItem.title
        holder.tvContent.text = currentItem.content
    }


    class NewsViewHolder(itemvView: View): RecyclerView.ViewHolder(itemvView){
        val tvFirstName = itemvView.tv_post_firstname
        val tvLastName = itemvView.tv_post_lastname
        val tvTitle = itemView.tv_post_title
        val tvContent = itemvView.tv_post_content
    }
}