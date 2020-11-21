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
import kotlin.math.log
import androidx.core.os.HandlerCompat.postDelayed
import android.os.Handler


class EventsFragment : Fragment() {

    private lateinit var viewModel: EventsViewModel

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

    private fun initUI(){
        val factory = InjectorUtils.provideEventsViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(EventsViewModel::class.java)

        val chosenAcademyId = activity!!.intent.extras!!.get("chosenAcademyId").toString()
        val loggedUserId = "user0001" //Todo take userid from shared preferences...

        val hideRefreshingIndicator = {
            swipe_refresh_layout.isRefreshing = false
        }

        viewModel.startListening(chosenAcademyId, loggedUserId, hideRefreshingIndicator)

        events_recycler_view.adapter = EventsAdapter(viewModel.getEvents(), viewModel)
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

}
