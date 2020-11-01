package lauks.sebastian.footballacademies.model.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*

class EventsDao {

    private val eventsList = mutableListOf<Event>()
    private val eventsLiveData = MutableLiveData<List<Event>>()

    init {
        val event1 = Event("001", "Mecz", Date(), "Wrocław", "Notatki", mutableListOf(),
            mutableListOf())
        eventsList.add(event1)
        eventsLiveData.value = eventsList
    }

    fun getEvents() = eventsLiveData as LiveData<List<Event>>

}