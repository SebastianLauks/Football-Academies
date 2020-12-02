package lauks.sebastian.footballacademies.viewmodel.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import lauks.sebastian.footballacademies.model.StorageDao
import lauks.sebastian.footballacademies.model.news.NewsRepository

class NewsViewModelFactory(private val newsRepository: NewsRepository, private val storageDao: StorageDao):
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsViewModel(newsRepository, storageDao) as T
    }
}