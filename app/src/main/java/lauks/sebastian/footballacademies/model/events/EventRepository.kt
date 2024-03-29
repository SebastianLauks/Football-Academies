package lauks.sebastian.footballacademies.model.events

class EventRepository private constructor(private val eventsDao: EventsDao) {


    fun removeEvent(eventId: String) = eventsDao.removeEvent(eventId)
    fun changePresence(eventId: String, presence: Boolean) = eventsDao.changePresence(eventId,presence)
    fun getEvents() = eventsDao.getEvents()
    fun getUserEventsFilters(updateEventsFiltersCheckboxes: (Boolean, Boolean, Boolean) -> Unit) = eventsDao.getUserEventsFilters(updateEventsFiltersCheckboxes)
    fun startListening(chosenAcademyId: String, loggedUserId: String, hideRefreshingIndicator: () -> Unit) = eventsDao.startListening(chosenAcademyId, loggedUserId, hideRefreshingIndicator)
    fun addEvent(authorId: String, type: String, date: Long, place: String, notes: String, placeLat: Double, placeLng: Double) =
        eventsDao.addEvent(authorId, type, date, place, notes, placeLat, placeLng)
    fun setUserEventsFilters(matches: Boolean, tournaments: Boolean, trainings: Boolean, finish: () -> Unit) = eventsDao.setUserEventsFilters(matches, tournaments, trainings, finish)
    fun fetchAllUsers(callback: () -> Unit) = eventsDao.fetchAllUsers(callback)
    fun fetchConfirmedParticipants(eventId: String, callback: () -> Unit ) = eventsDao.fetchConfirmedParticipants(eventId, callback)
    fun getAllUsers() = eventsDao.getAllUsers()
    fun getConfirmedUsers() = eventsDao.getConfirmedUsers()


    companion object {
        @Volatile
        private var instance: EventRepository? = null

        fun getInstance(eventsDao: EventsDao) =
            instance ?: synchronized(this) {
                instance ?: EventRepository(eventsDao).also { instance = it }
            }
    }
}