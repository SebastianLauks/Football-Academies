package lauks.sebastian.footballacademies.viewmodel.news

import androidx.lifecycle.ViewModel
import lauks.sebastian.footballacademies.model.news.NewsRepository

class NewsViewModel(private val newsRepository: NewsRepository): ViewModel() {

    fun getNewss() = newsRepository.getNewss()
}