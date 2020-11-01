package lauks.sebastian.footballacademies.view.events


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

/**
 * A simple [Fragment] subclass.
 */
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

    fun initUI(){
        val factory = InjectorUtils.provideEventsViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(EventsViewModel::class.java)


        events_recycler_view.adapter = EventsAdapter(viewModel.getEvents())
        events_recycler_view.layoutManager = LinearLayoutManager(activity)
        events_recycler_view.setHasFixedSize(true)
        viewModel.getEvents().observe(this, Observer {
            (events_recycler_view.adapter as EventsAdapter).notifyDataSetChanged()
        })

    }

}
