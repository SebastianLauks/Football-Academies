package lauks.sebastian.footballacademies.model.academy

class AcademyRepository private constructor(private val academyDao: AcademyDao){

    fun addAcademy(academy: Academy) {
        academyDao.addAcademy(academy)
    }

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