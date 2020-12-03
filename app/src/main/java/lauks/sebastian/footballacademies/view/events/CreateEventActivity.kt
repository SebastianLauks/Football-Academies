package lauks.sebastian.footballacademies.view.events

import android.app.ActionBar
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_create_event.*
import lauks.sebastian.footballacademies.R
import lauks.sebastian.footballacademies.utilities.InjectorUtils
import lauks.sebastian.footballacademies.viewmodel.events.EventsViewModel
import java.text.SimpleDateFormat
import java.util.*

class CreateEventActivity : AppCompatActivity() {

    val cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Warsaw"))
    private lateinit var viewModel: EventsViewModel
    private lateinit var loggedUserId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        val factory = InjectorUtils.provideEventsViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(EventsViewModel::class.java)

        val sharedPref = getSharedPreferences(resources.getString(R.string.app_name), Context.MODE_PRIVATE)
        loggedUserId = sharedPref.getString("loggedUserId", "unknown").toString()

        bt_event_cancel.setOnClickListener {
            finish()
        }

        bt_events_add.setOnClickListener {
            val toast = Toast.makeText(applicationContext, "", Toast.LENGTH_SHORT)

            when {
                spinner_events_type.selectedItemPosition == 0 -> {
                    toast.setText(R.string.create_event_empty_type)
                    toast.show()
                }

                et_events_place.text.isEmpty() -> {
                    toast.setText(R.string.create_event_empty_place)
                    toast.show()
                }
                et_events_date.text.isEmpty() -> {
                    toast.setText(R.string.create_event_empty_date)
                    toast.show()
                }
                et_events_time.text.isEmpty() -> {
                    toast.setText(R.string.create_event_empty_time)
                    toast.show()
                }
                else -> {
                    Log.d("dataaa", et_events_time.text.toString())
                    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
                    viewModel.addEvent(
                        loggedUserId,
                        resources.getStringArray(R.array.event_types_array)[spinner_events_type.selectedItemPosition],
                        SimpleDateFormat("dd/MM/yyyy HH:mm").parse(et_events_date.text.toString() + " " + et_events_time.text.toString()).time,
                        et_events_place.text.toString(),
                        et_events_notes.text.toString())

                    spinner_events_type.setSelection(0)
                    et_events_date.setText("")
                    et_events_time.setText("")
                    et_events_place.setText("")
                    et_events_notes.setText("")


                    toast.setText(R.string.create_event_added)
                    toast.show()

                    finish()
                }
            }
        }

        setUpSpinner()
        setUpDatePicker()
        setUpTimePicker()
        setUpPlacePicker()


    }

    private  fun setUpPlacePicker(){
        et_events_place.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)

        }
    }

    private fun setUpTimePicker() {
        val timeSetListener = object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                cal.set(Calendar.MINUTE, minute)
                et_events_time.setText(SimpleDateFormat("HH:mm", Locale.US).format(cal.time))
            }
        }

        et_events_time.setOnClickListener {
            TimePickerDialog(
                this@CreateEventActivity, timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }
    }


    private fun setUpDatePicker() {
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }

        et_events_date.setOnClickListener {
            DatePickerDialog(
                this@CreateEventActivity, dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun updateDateInView() {
        val format = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(format, Locale.US)
        et_events_date.setText(sdf.format(cal.time))
    }


    private fun setUpSpinner() {
        val types = resources.getStringArray(R.array.event_types_array)
//        val spinnerAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, types)

        val spinnerAdapter =
            object : ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, types) {

                override fun isEnabled(position: Int): Boolean {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return position != 0
                }

                override fun getDropDownView(
                    position: Int,
                    convertView: View?,
                    parent: ViewGroup
                ): View {
                    val view: TextView =
                        super.getDropDownView(position, convertView, parent) as TextView
                    if (position == 0) {
                        view.setTextColor(Color.GRAY)
                    }
//                else{
////                    view.setTextColor(Color.BLACK)
////                }
                    return view
                }

            }
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner_events_type.adapter = spinnerAdapter

        spinner_events_type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val value = parent!!.getItemAtPosition(position).toString()
                if (value == types[0]) {
                    (view as TextView).setTextColor(Color.GRAY)
                }
            }

        }


//        ArrayAdapter.createFromResource(this, R.array.event_types_array, android.R.layout.simple_spinner_item).also { adapter ->
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            spinner_events_type.adapter = adapter
//        }

    }
}
