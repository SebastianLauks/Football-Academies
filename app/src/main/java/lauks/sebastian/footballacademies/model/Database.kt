package lauks.sebastian.footballacademies.model

import lauks.sebastian.footballacademies.model.academy.AcademyDao
import lauks.sebastian.footballacademies.model.events.EventsDao
import lauks.sebastian.footballacademies.model.news.NewsDao
import lauks.sebastian.footballacademies.model.squad.SquadDao

class Database private constructor(){

    //All DAOs here
    val academyDao = AcademyDao()
    val newsDao = NewsDao()
    val squadDao = SquadDao()
    val eventsDao = EventsDao()


    companion object{
        // @Volatile - Writes to this property are immediately visible to other threads
        @Volatile private var instance: Database? = null

        // The only way to get hold of the FakeDatabase object
        fun getInstance() =
        // Already instantiated? - return the instance
            // Otherwise instantiate in a thread-safe manner
            instance ?: synchronized(this) {
                // If it's still not instantiated, finally create an object
                // also set the "instance" property to be the currently created one
                instance ?: Database().also { instance = it }
            }
    }

}