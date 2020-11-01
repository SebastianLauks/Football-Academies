package lauks.sebastian.footballacademies.viewmodel.events

import androidx.lifecycle.ViewModel
import lauks.sebastian.footballacademies.model.events.EventRepository

class EventsViewModel(private val eventRepository: EventRepository):ViewModel() {

    fun getEvents() = eventRepository.getEvents()
}