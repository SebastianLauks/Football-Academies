package lauks.sebastian.footballacademies.model.events

class EventRepository private constructor(private val eventsDao: EventsDao) {


    fun changePresence(eventId: String, presence: Boolean) = eventsDao.changePresence(eventId,presence)
    fun getEvents() = eventsDao.getEvents()
    fun startListening(chosenAcademyId: String, loggedUserId: String, hideRefreshingIndicator: () -> Unit) = eventsDao.startListening(chosenAcademyId, loggedUserId, hideRefreshingIndicator)
    fun addEvent(authorId: String, type: String, date: Long, place: String, notes: String) =
        eventsDao.addEvent(authorId, type, date, place, notes)

    companion object {
        @Volatile
        private var instance: EventRepository? = null

        fun getInstance(eventsDao: EventsDao) =
            instance ?: synchronized(this) {
                instance ?: EventRepository(eventsDao).also { instance = it }
            }
    }
}