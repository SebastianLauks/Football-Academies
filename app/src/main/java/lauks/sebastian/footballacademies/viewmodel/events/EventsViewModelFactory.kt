package lauks.sebastian.footballacademies.viewmodel.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import lauks.sebastian.footballacademies.model.events.EventRepository

class EventsViewModelFactory (private val eventRepository: EventRepository):
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EventsViewModel(eventRepository) as T
    }
}