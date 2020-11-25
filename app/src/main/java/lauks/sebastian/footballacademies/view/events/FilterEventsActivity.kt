package lauks.sebastian.footballacademies.view.events

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_filter_events.*
import lauks.sebastian.footballacademies.R
import lauks.sebastian.footballacademies.utilities.InjectorUtils
import lauks.sebastian.footballacademies.viewmodel.events.EventsViewModel

class FilterEventsActivity : AppCompatActivity() {

    private lateinit var viewModel: EventsViewModel
    private lateinit var loadingIncicator: ProgressBar
    private lateinit var layout1: LinearLayout
    private lateinit var layout2: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_events)

//        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)

        val factory = InjectorUtils.provideEventsViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(EventsViewModel::class.java)

        viewModel.getUserEventsFilters(updateCheckboxes)
        //This state of checkboxes should be saved somewhere and updated after start this activity

        layout1 = findViewById(R.id.layout_1)
        layout2 = findViewById(R.id.layout_2)
        setupLoadingIndicator()

        bt_events_cancel.setOnClickListener {
            finish()
        }

        bt_events_apply.setOnClickListener {
            val fin = {finish()}
            viewModel.setUserEventsFilters(cb_events_matches.isChecked,cb_events_tournaments.isChecked,cb_events_trainings.isChecked, fin)

        }
    }
    fun setupLoadingIndicator(){
        loadingIncicator = findViewById(R.id.progressBar1)
        loadingIncicator.visibility = View.VISIBLE
        layout1.visibility = View.GONE
        layout2.visibility = View.GONE
    }

    private val updateCheckboxes = {matches: Boolean, tournaments: Boolean, trainings: Boolean ->
        cb_events_matches.isChecked = matches
        cb_events_tournaments.isChecked = tournaments
        cb_events_trainings.isChecked = trainings

        layout1.visibility = View.VISIBLE
        layout2.visibility = View.VISIBLE
        loadingIncicator.visibility = View.GONE
    }
}
