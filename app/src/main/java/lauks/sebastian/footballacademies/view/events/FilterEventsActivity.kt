package lauks.sebastian.footballacademies.view.events

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import kotlinx.android.synthetic.main.activity_filter_events.*
import lauks.sebastian.footballacademies.R

class FilterEventsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_events)

//        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)


        updateCheckboxes()
        //This state of checkboxes should be saved somewhere and updated after start this activity

        bt_events_cancel.setOnClickListener {
            finish()
        }

        bt_events_apply.setOnClickListener {
            // HERE

            finish()
        }
    }

    private fun updateCheckboxes(){
        cb_events_matches.isChecked = true
        cb_events_tournaments.isChecked = true
        cb_events_trainings.isChecked = true
    }
}
