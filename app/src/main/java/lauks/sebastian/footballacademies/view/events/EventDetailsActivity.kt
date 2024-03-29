package lauks.sebastian.footballacademies.view.events

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_event_details.*
import lauks.sebastian.footballacademies.R
import lauks.sebastian.footballacademies.model.Player
import lauks.sebastian.footballacademies.model.events.Event
import lauks.sebastian.footballacademies.utilities.InjectorUtils
import lauks.sebastian.footballacademies.viewmodel.events.EventsViewModel
import java.text.SimpleDateFormat
import java.util.*

class EventDetailsActivity : AppCompatActivity() {

    private lateinit var viewModel: EventsViewModel
    private lateinit var event: Event
    private lateinit var allUsers: LiveData<List<Player>>
    private lateinit var confirmedUsers: LiveData<List<Player>>
    private var usersToDisplay = MutableLiveData<List<Player>>()
    private lateinit var spinner: Spinner
    private lateinit var loggedUserId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        val factory = InjectorUtils.provideEventsViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(EventsViewModel::class.java)

        event = intent.extras!!.get("event") as Event
        refreshLayout()
        allUsers = viewModel.getAllUsers()
        confirmedUsers = viewModel.getConfirmedUsers()
        usersToDisplay.value = listOf()

        val sharedPref = getSharedPreferences(
            resources.getString(R.string.app_name),
            Context.MODE_PRIVATE
        )
        loggedUserId = sharedPref.getString("loggedUserId", "unknown").toString()

        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        rv_event_details.adapter = EventDetailsAdapter(usersToDisplay, onItemClick)
        usersToDisplay.observe(this, Observer {
            (rv_event_details.adapter as EventDetailsAdapter).notifyDataSetChanged()
        })

        val linearLayoutManager = LinearLayoutManager(this)
//        linearLayoutManager.reverseLayout = true
//        linearLayoutManager.stackFromEnd = true
        rv_event_details.layoutManager = linearLayoutManager
        rv_event_details.setHasFixedSize(true)

        setupItemsViews()
        setupSpinner()
        setupMapButton()

        swipe_refresh_layout.setOnRefreshListener {
            refreshLayout()
        }


//        setupSwitchButton()

    }

//    private fun setupSwitchButton() {
//        switch_event_details.isChecked =
//            confirmedUsers.value!!.any { player -> player.id == loggedUserId }
//        switch_event_details.setOnCheckedChangeListener { buttonView, isChecked ->
//            viewModel.changePresence(event.id, isChecked)
//            refreshLayout()
//        }
//    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setupItemsViews() {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
        tv_event_details_timestamp.text = formatter.format(Date(event.date))
        tv_event_details_place.text = event.place
        tv_event_details_content.text = event.notes
//        supportActionBar!!.title = event.type
        tv_event_details_type.text = event.type
    }

    private fun setupSpinner() {
        spinner = spinner_event_details
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.events_participants,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        usersToDisplay.value = confirmedUsers.value
                    }
                    1 -> {
                        usersToDisplay.value =
                            allUsers.value!!.filter { user -> !confirmedUsers.value!!.any { it.id == user.id } }
                    }
                }

            }
        }
    }

    private fun setupMapButton(){
        bt_map.setOnClickListener {
            onMapButtonClicked(event.placeLat, event.placeLng)
        }
    }
    private val onMapButtonClicked = { lat: Double?, lng: Double? ->
        if(lat != null && lng != null){
            val uri = "http://maps.google.com/maps?daddr=$lat,$lng"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            startActivity(intent)
        }else{
            Toast.makeText(applicationContext, "Brak danych geolokalizacyjnych", Toast.LENGTH_SHORT).show()
        }

    }

    private fun setUsersToDisplay() {
        when (spinner.selectedItemPosition) {
            0 -> {
                usersToDisplay.value = confirmedUsers.value
            }
            1 -> {
                usersToDisplay.value =
                    allUsers.value!!.filter { user -> !confirmedUsers.value!!.any { it.id == user.id } }
            }
        }
    }

    private fun refreshLayout() {
        swipe_refresh_layout.isRefreshing = true
        viewModel.fetchAllUsers {
            viewModel.fetchConfirmedParticipants(event.id) {
                allUsers = viewModel.getAllUsers()
                confirmedUsers = viewModel.getConfirmedUsers()
                swipe_refresh_layout.isRefreshing = false
                setUsersToDisplay()
            }
        }
    }

    private val onItemClick = { userId: String ->
        //ToDo open player details
    }
}
