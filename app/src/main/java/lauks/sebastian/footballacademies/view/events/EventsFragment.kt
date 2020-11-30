package lauks.sebastian.footballacademies.view.events


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_events.*

import lauks.sebastian.footballacademies.R
import lauks.sebastian.footballacademies.utilities.InjectorUtils
import lauks.sebastian.footballacademies.viewmodel.events.EventsViewModel
import android.widget.Toast
import lauks.sebastian.footballacademies.model.events.Event
import lauks.sebastian.footballacademies.utilities.CustomDialogGenerator


class EventsFragment : Fragment() {

    private lateinit var viewModel: EventsViewModel
    private lateinit var hideRefreshingIndicator: () -> Unit
    private lateinit var chosenAcademyId: String
    private lateinit var loggedUserId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
    }

    override fun onResume() {
        super.onResume()
        refreshListOfEvents()
    }

    private fun refreshListOfEvents(){
        swipe_refresh_layout.isRefreshing = true
        viewModel.startListening(chosenAcademyId, loggedUserId, hideRefreshingIndicator)
    }

    private fun initUI(){
        val factory = InjectorUtils.provideEventsViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(EventsViewModel::class.java)

        chosenAcademyId = activity!!.intent.extras!!.get("chosenAcademyId").toString()
        loggedUserId = "user0001" //Todo take userid from shared preferences...

        hideRefreshingIndicator = {
            swipe_refresh_layout.isRefreshing = false
        }

        refreshListOfEvents()

        events_recycler_view.adapter = EventsAdapter(viewModel.getEvents(), onChangePresence, onEventLongClick, onEventClick)
        val linearLayoutManager = LinearLayoutManager(activity)
//        linearLayoutManager.reverseLayout = true
//        linearLayoutManager.stackFromEnd = true
        events_recycler_view.layoutManager = linearLayoutManager
        events_recycler_view.setHasFixedSize(true)

        swipe_refresh_layout.setOnRefreshListener {

            viewModel.startListening(chosenAcademyId, loggedUserId, hideRefreshingIndicator)
//            Handler().postDelayed(Runnable {
//                // Stop animation (This will be after 3 seconds)
//
//            }, 4000) // Delay in millis
        }
        viewModel.getEvents().observe(this, Observer {
            (events_recycler_view.adapter as EventsAdapter).notifyDataSetChanged()
        })

        setupFav()

    }

    private fun setupFav(){
        val fab = events_fab_create
        fab.setOnClickListener {
            val intent = Intent(context, CreateEventActivity::class.java)
            startActivity(intent)
        }
    }

    private val onChangePresence = { eventId:String, presence: Boolean ->
        viewModel.changePresence(eventId, presence)

    }

    private val onEventClick ={ event: Event ->
        val intent = Intent(context, EventDetailsActivity::class.java)
        intent.putExtra("event", event)
        startActivity(intent)
    }

    private val onEventLongClick = { id: String ->

        val event: Event? = viewModel.getEvents().value!!.find { event ->
            event.id == id
        }
        if (event != null && event.authorId == loggedUserId) {
            CustomDialogGenerator.createCustomDialog(
                context!!,
                "Czy chcesz usunąć wybrane wydarzenie?",
                "Tak",
                "Nie"
            ) {
                viewModel.removeEvent(event.id)
                Toast.makeText(context, "Wydarzenie zostało usunięte", Toast.LENGTH_SHORT).show()
                refreshListOfEvents()
            }
        } else {
            Toast.makeText(context, "Nie można usuwać cudzych wydarzeń", Toast.LENGTH_SHORT).show()
        }
    }

}
