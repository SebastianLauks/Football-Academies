package lauks.sebastian.footballacademies.model.news

import lauks.sebastian.footballacademies.model.academy.AcademyDao
import lauks.sebastian.footballacademies.model.academy.AcademyRepository

class NewsRepository private constructor(private val newsDao: NewsDao){


    fun getNewss() = newsDao.getNewss()

    companion object{
        // Singleton instantiation you already know and love
        @Volatile private var instance: NewsRepository? = null

        fun getInstance(newsDao: NewsDao) =
            instance ?: synchronized(this) {
                instance ?: NewsRepository(newsDao).also { instance = it }
            }
    }


}