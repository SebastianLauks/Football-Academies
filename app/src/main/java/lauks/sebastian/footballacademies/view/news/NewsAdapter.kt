package lauks.sebastian.footballacademies.view.news

import android.app.ActionBar
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.MediaController
import androidx.core.os.persistableBundleOf
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.news_item.view.*
import lauks.sebastian.footballacademies.R
import lauks.sebastian.footballacademies.model.profile.User
import lauks.sebastian.footballacademies.model.news.News
import lauks.sebastian.footballacademies.viewmodel.news.NewsViewModel
import java.io.ByteArrayInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class NewsAdapter(
    private val newsList: LiveData<List<News>>,
    private val usersList: LiveData<List<User>>,
    private val onPostLongClick: (name: String) -> Unit,
    private val scrollToPosition: (position: Int) -> Unit,
    private val viewModel: NewsViewModel
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private lateinit var context: Context
    private var mExpandedPosition = -1
    private var previousEpandedPosition = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        val holder = NewsViewHolder(itemView)

        context = parent.context
        //HERE e.g. holder.itemView.setOnClickListener{....
        holder.itemView.setOnLongClickListener {
            if (holder.adapterPosition != RecyclerView.NO_POSITION) {
//                onPostLongClick.invoke(newsList.value!![holder.adapterPosition].id)
                onPostLongClick(newsList.value!![holder.adapterPosition].id)
            }
            true
        }


        return holder
    }

    override fun getItemCount() = newsList.value!!.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = newsList.value!![position]
        val usersList = usersList.value!!
        holder.tvFirstName.text =
            usersList.find { user -> user.id == currentItem.authorId }?.firstname
        holder.tvLastName.text =
            usersList.find { user -> user.id == currentItem.authorId }?.lastname
        holder.tvTitle.text = currentItem.title
        holder.tvContent.text = currentItem.content
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
        holder.tvCreationDate.text = formatter.format(Date(currentItem.creationDate))


//        if (currentItem.imageUrl != null && currentItem.imageUrl != "") {
        val isExpanded: Boolean = position == mExpandedPosition

//            holder.layout.visibility = if(isExpanded) View.VISIBLE else View.GONE
        var colapsedHeight = 250


        holder.ivPost.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        holder.ivPost.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT

        holder.layout.layoutParams.height =
            if (isExpanded) ViewGroup.LayoutParams.WRAP_CONTENT else colapsedHeight
        holder.arrowIcon.rotation = if (isExpanded) 180f else 0f


//        var gravityy = if (isExpanded) Gravity.CENTER_HORIZONTAL else Gravity.START
//        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, if (isExpanded) ViewGroup.LayoutParams.WRAP_CONTENT else colapsedHeight)
//        params.gravity = gravityy
//        holder.layout.layoutParams = params

        if (isExpanded)
            previousEpandedPosition = position

        holder.itemView.setOnClickListener {
            mExpandedPosition = if (isExpanded) -1 else position
            notifyItemChanged(previousEpandedPosition)
            notifyItemChanged(position)
            scrollToPosition(position)

//                if( holder.layout.layoutParams.height == 0){
//                    holder.layout.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
//                    holder.arrowIcon.rotation = 180f
////            if(holder.layout.visibility == View.VISIBLE){
//
////                holder.layout.visibility = View.GONE
//                }else{
//                    holder.layout.layoutParams.height = 0
//                    holder.arrowIcon.rotation = 0f
////                holder.layout.visibility =  View.VISIBLE
//                }
////            notifyDataSetChanged()
//                notifyItemChanged(position)
        }



        holder.arrowIcon.visibility = View.VISIBLE
//            holder.postHint.visibility = View.VISIBLE
        holder.progressBar.visibility = View.VISIBLE

        holder.videoView.visibility = View.GONE
        holder.ivPost.visibility = View.GONE

        holder.ivPost.setImageDrawable(null)
        if (currentItem.imageUrl != null && currentItem.imageUrl != "") {
            Picasso.get().load(currentItem.imageUrl)
                .into(holder.ivPost, object : com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        //set animations here
                        holder.ivPost.visibility = View.VISIBLE
                        holder.progressBar.visibility = View.GONE
                    }

                    override fun onError(e: java.lang.Exception?) {
                        holder.progressBar.visibility = View.GONE

                    }
                })
        } else if (currentItem.videoName != null && currentItem.videoName != "") {
            if(isExpanded){
                val mediaController = MediaController(holder.videoView.context)
                mediaController.setAnchorView(holder.videoView)
                holder.videoView.visibility = View.VISIBLE
                holder.videoView.setMediaController(mediaController)
                holder.videoView.setVideoPath(currentItem.videoUrl)
                holder.videoView.setOnPreparedListener {
                    //                    holder.videoView.stopPlayback()
                    holder.progressBar.visibility = View.GONE
                }

//            holder.videoView.start()


                holder.videoView.setOnClickListener {

                }
// in order to not collapse card when click on video.
            }else{
                holder.ivPost.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                holder.ivPost.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                holder.ivPost.setImageResource(R.drawable.video)
                holder.ivPost.visibility = View.VISIBLE
                holder.progressBar.visibility = View.GONE
            }

//            if (!isExpanded) holder.videoView.stopPlayback()

//            viewModel.downloadVideo(currentItem.videoName){success, data ->
//                val input = ByteArrayInputStream(data)
//                val output = FileOutputStream(currentItem.videoName)
//                val dataList = ByteArray(4096)
//                var count: Int
//                do{
//                    count = (input.read(dataList))
//
//                }
//                while(count != -1)
//                holder.videoView.setVideoPath(currentItem.videoName)
//            }

        } else {
            holder.layout.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            holder.progressBar.visibility = View.GONE
        }

//        } else {
//            holder.progressBar.visibility = View.GONE
//            holder.ivPost.visibility = View.GONE
//            holder.arrowIcon.visibility = View.GONE
//        }


//        if(currentItem.imageName != null)
//            viewModel.downloadImage(currentItem.imageName.toString()){success, data ->
//                if(success){
//                    val bitmap = BitmapFactory.decodeByteArray(data, 0, data!!.size)
//                    holder.ivPost.setImageBitmap(bitmap)
//                }else{
//
//                }
//            }

    }


    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvFirstName = itemView.tv_post_firstname
        val tvLastName = itemView.tv_post_lastname
        val tvTitle = itemView.tv_post_title
        val tvContent = itemView.tv_post_content
        val tvCreationDate = itemView.tv_post_date
        val ivPost = itemView.iv_post
        val progressBar = itemView.progressBar1
        val cardView = itemView.news_card_view
        val layout = itemView.linear
        val arrowIcon = itemView.arrow_icon
        val postHint = itemView.tv_post_hint
        val videoView = itemView.video_post
    }
}