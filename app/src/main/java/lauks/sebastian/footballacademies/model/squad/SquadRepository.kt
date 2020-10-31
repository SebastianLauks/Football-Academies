package lauks.sebastian.footballacademies.model.squad

class SquadRepository private constructor(private val squadDao: SquadDao){

    fun getPlayers() = squadDao.getPlayers()

    companion object{
        // Singleton instantiation you already know and love
        @Volatile private var instance: SquadRepository? = null

        fun getInstance(squadDao: SquadDao) =
            instance ?: synchronized(this) {
                instance ?: SquadRepository(squadDao).also { instance = it }
            }
    }
}