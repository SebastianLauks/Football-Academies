package lauks.sebastian.footballacademies.viewmodel.events

import androidx.lifecycle.ViewModel
import lauks.sebastian.footballacademies.model.events.EventRepository

class EventsViewModel(private val eventRepository: EventRepository):ViewModel() {

    fun changePresence(eventId: String, presence: Boolean) = eventRepository.changePresence(eventId,presence)
    fun startListening(chosenAcademyId: String, loggedUserId: String, hideRefreshingIndicator: () -> Unit) = eventRepository.startListening(chosenAcademyId, loggedUserId, hideRefreshingIndicator)
    fun addEvent(authorId: String, type: String, date: Long, place: String, notes: String) = eventRepository.addEvent( authorId, type, date, place, notes)
    fun getEvents() = eventRepository.getEvents()
}