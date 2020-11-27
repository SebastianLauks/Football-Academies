package lauks.sebastian.footballacademies.model.news

import lauks.sebastian.footballacademies.model.academy.AcademyDao
import lauks.sebastian.footballacademies.model.academy.AcademyRepository
import java.util.*

class NewsRepository private constructor(private val newsDao: NewsDao){

    fun startListening(chosenAcademyId:String, hideRefreshingIndicator: () -> Unit) = newsDao.startListening(chosenAcademyId, hideRefreshingIndicator)

    fun getNewss() = newsDao.getNewss()
    fun getUsers() = newsDao.getUsers()

    fun addNews(authorId: String, title: String, content: String, creationDate: Date){
        newsDao.addNews(authorId, title, content, creationDate)
    }
    fun removeNews(newsId: String) = newsDao.removeNews(newsId)

    companion object{
        // Singleton instantiation you already know and love
        @Volatile private var instance: NewsRepository? = null

        fun getInstance(newsDao: NewsDao) =
            instance ?: synchronized(this) {
                instance ?: NewsRepository(newsDao).also { instance = it }
            }
    }


}