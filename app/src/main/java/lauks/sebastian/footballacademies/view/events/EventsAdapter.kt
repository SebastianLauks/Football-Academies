package lauks.sebastian.footballacademies.view.events

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.event_item.view.*
import lauks.sebastian.footballacademies.R
import lauks.sebastian.footballacademies.model.events.Event
import lauks.sebastian.footballacademies.viewmodel.events.EventsViewModel
import java.text.SimpleDateFormat
import java.util.*

class EventsAdapter(
    private val eventsList: LiveData<List<Event>>,
    private val onChangePresence: (eventId: String, presence: Boolean) -> Unit,
    private val onEventLongClick: (eventId: String) -> Unit,
    private val onEventClick: (event: Event) -> Unit
) : RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {

    private lateinit var loggedUserId: String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)
        val holder = EventViewHolder(itemView)

        //HERE e.g. holder.itemView.setOnClickListener{....
        holder.switchButton.setOnCheckedChangeListener { buttonView, isChecked ->
            onChangePresence(eventsList.value!![holder.adapterPosition].id, isChecked)
        }

        holder.itemView.setOnLongClickListener {
            onEventLongClick(eventsList.value!![holder.adapterPosition].id)
            true
        }

        holder.itemView.setOnClickListener {
            onEventClick(eventsList.value!![holder.adapterPosition])
        }

        val sharedPref = parent.context.getSharedPreferences(parent.context.resources.getString(R.string.app_name),
            Context.MODE_PRIVATE)
        loggedUserId = sharedPref.getString("loggedUserId", "unknown").toString()

        return holder

    }

    override fun getItemCount() = eventsList.value!!.size

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val currentItem = eventsList.value!![position]

        //HERE probably should be id of current logged user to know what value set for switch button
        //This id of current logged user should be compared with two lists in the current item - with confirmed users and unconfirmed user
        //to decide what value of switch button should be
        //Maybe in the constructor on EventsAdapter should be one more parameter with that id of current logged user. I have to think about it
        //This is written 01.11.20 at 23:31. Take it into account

        //holder.switchButton ????

        holder.switchButton.isChecked = eventsList.value!![position].confirmedParticipants.contains(loggedUserId)

        holder.tvType.text = currentItem.type
        holder.tvPlace.text = currentItem.place
        holder.tvContent.text = currentItem.notes
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
        holder.tvTimestamp.text = formatter.format(Date(currentItem.date))


    }


    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvType = itemView.tv_event_type
        val tvPlace = itemView.tv_event_place
        val tvTimestamp = itemView.tv_event_timestamp
        val tvContent = itemView.tv_event_content
        val switchButton = itemView.switch_event


    }
}