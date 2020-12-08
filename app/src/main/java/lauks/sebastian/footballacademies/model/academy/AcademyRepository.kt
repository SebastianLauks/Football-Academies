package lauks.sebastian.footballacademies.model.academy

class AcademyRepository private constructor(private val academyDao: AcademyDao){

    fun leaveAcademy(academyId: String, userId: String, refreshAcademies: () -> Unit) = academyDao.leaveAcademy(academyId, userId,refreshAcademies)
    fun addAcademy(academyName: String,callback: ()-> Unit) = academyDao.addAcademy(academyName,callback)


    fun addToSquad(academyCode: String, joinAcademyCallback: (joined: Boolean) -> Unit) = academyDao.addToSquad(academyCode, joinAcademyCallback)

    fun startListening(loggedUserId: String, hideRefreshingIncdicator: () -> Unit) = academyDao.startListening(loggedUserId, hideRefreshingIncdicator)
    fun fetchAcademy(academyId: String, callback: (academy: Academy) -> Unit) = academyDao.fetchAcademy(academyId, callback)
    fun getAcademies() = academyDao.getAcademies()
    fun generateNewCode(academyId: String, callback: () -> Unit) = academyDao.generateNewCode(academyId,callback)

    companion object{
        // Singleton instantiation you already know and love
        @Volatile private var instance: AcademyRepository? = null

        fun getInstance(academyDao: AcademyDao) =
            instance ?: synchronized(this) {
                instance ?: AcademyRepository(academyDao).also { instance = it }
            }
    }

}