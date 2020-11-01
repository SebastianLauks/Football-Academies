package lauks.sebastian.footballacademies.model.events

class EventRepository private constructor(private val eventsDao: EventsDao) {


    fun getEvents() = eventsDao.getEvents()

    companion object {
        @Volatile
        private var instance: EventRepository? = null

        fun getInstance(eventsDao: EventsDao) =
            instance ?: synchronized(this) {
                instance ?: EventRepository(eventsDao).also { instance = it }
        }
    }
}