package lauks.sebastian.footballacademies.viewmodel.news

import androidx.lifecycle.ViewModel
import lauks.sebastian.footballacademies.model.news.NewsRepository
import java.util.*

class NewsViewModel(private val newsRepository: NewsRepository): ViewModel() {

    fun getNewss() = newsRepository.getNewss()

    fun startListening(chosenAcademyId: String) = newsRepository.startListening(chosenAcademyId)

    fun addNews(authorId: String, title: String, content: String, creationDate: Date){
        newsRepository.addNews(authorId, title, content, creationDate)
    }
}