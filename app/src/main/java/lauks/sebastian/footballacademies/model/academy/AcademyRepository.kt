package lauks.sebastian.footballacademies.model.academy

class AcademyRepository private constructor(private val academyDao: AcademyDao){

    fun addAcademy(academyName: String) {
        val academy = Academy("-1",academyName)
        academyDao.addAcademy(academy)
    }

    fun startListening() = academyDao.startListening()

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