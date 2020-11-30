package lauks.sebastian.footballacademies.view.events

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_event_details.view.*
import kotlinx.android.synthetic.main.event_details_player_item.view.*
import lauks.sebastian.footballacademies.R
import lauks.sebastian.footballacademies.model.Player

class EventDetailsAdapter(
    private val usersList: LiveData<List<Player>>,
    private val onItemClick: (userId: String) -> Unit
): RecyclerView.Adapter<EventDetailsAdapter.EventDetailsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventDetailsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.event_details_player_item, parent, false)
        val holder = EventDetailsViewHolder(itemView)


        //HERE will be on item click call
//        holder.itemView.setOnClickListener {
//            onItemClick()
//        }
        return holder
    }

    override fun getItemCount() = usersList.value!!.size

    override fun onBindViewHolder(holder: EventDetailsViewHolder, position: Int) {
        holder.firstname.text = usersList.value!![position].firstname
        holder.lastname.text = usersList.value!![position].lastname


    }


    class EventDetailsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val firstname = itemView.tv_event_details_item_firstname
        val lastname = itemView.tv_event_details_item_lastname
    }
}