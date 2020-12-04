package lauks.sebastian.footballacademies.viewmodel.events

import androidx.lifecycle.ViewModel
import lauks.sebastian.footballacademies.model.events.EventRepository

class EventsViewModel(private val eventRepository: EventRepository):ViewModel() {

    fun removeEvent(eventId: String) = eventRepository.removeEvent(eventId)
    fun changePresence(eventId: String, presence: Boolean) = eventRepository.changePresence(eventId,presence)
    fun startListening(chosenAcademyId: String, loggedUserId: String, hideRefreshingIndicator: () -> Unit) = eventRepository.startListening(chosenAcademyId, loggedUserId, hideRefreshingIndicator)
    fun addEvent(authorId: String, type: String, date: Long, place: String, notes: String, placeLat: Double, placeLng: Double) = eventRepository.addEvent( authorId, type, date, place, notes, placeLat, placeLng)
    fun getEvents() = eventRepository.getEvents()
    fun getUserEventsFilters(updateEventsFiltersCheckboxes: (Boolean, Boolean, Boolean) -> Unit) = eventRepository.getUserEventsFilters(updateEventsFiltersCheckboxes)
    fun setUserEventsFilters(matches: Boolean, tournaments: Boolean, trainings: Boolean, finish: () -> Unit) = eventRepository.setUserEventsFilters(matches, tournaments, trainings, finish )
    fun fetchAllUsers(callback: () -> Unit) = eventRepository.fetchAllUsers(callback)
    fun fetchConfirmedParticipants(eventId: String, callback: () -> Unit ) = eventRepository.fetchConfirmedParticipants(eventId, callback)
    fun getAllUsers() = eventRepository.getAllUsers()
    fun getConfirmedUsers() = eventRepository.getConfirmedUsers()
}