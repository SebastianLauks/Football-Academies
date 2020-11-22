package lauks.sebastian.footballacademies.model.academy

class AcademyRepository private constructor(private val academyDao: AcademyDao){

    fun addAcademy(academyName: String) {

        academyDao.addAcademy(academyName)
    }

    fun addToSquad(academyCode: String) = academyDao.addToSquad(academyCode)

    fun startListening(loggedUserId: String, hideRefreshingIncdicator: () -> Unit) = academyDao.startListening(loggedUserId, hideRefreshingIncdicator)

    fun getAcademies() = academyDao.getAcademies()

    companion object{
        // Singleton instantiation you already know and love
        @Volatile private var instance: AcademyRepository? = null

        fun getInstance(academyDao: AcademyDao) =
            instance ?: synchronized(this) {
                instance ?: AcademyRepository(academyDao).also { instance = it }
            }
    }

}