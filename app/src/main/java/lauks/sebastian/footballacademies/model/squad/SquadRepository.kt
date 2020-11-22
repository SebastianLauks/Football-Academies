package lauks.sebastian.footballacademies.model.squad

class SquadRepository private constructor(private val squadDao: SquadDao){

    fun getPlayers() = squadDao.getPlayers()
    fun fetchPlayers(academyId: String, hideRefreshingIndicator: () -> Unit) = squadDao.fetchPlayers(academyId, hideRefreshingIndicator)

    companion object{
        // Singleton instantiation you already know and love
        @Volatile private var instance: SquadRepository? = null

        fun getInstance(squadDao: SquadDao) =
            instance ?: synchronized(this) {
                instance ?: SquadRepository(squadDao).also { instance = it }
            }
    }
}