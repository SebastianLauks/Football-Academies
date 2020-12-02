package lauks.sebastian.footballacademies.viewmodel.news

import android.net.Uri
import androidx.lifecycle.ViewModel
import lauks.sebastian.footballacademies.model.StorageDao
import lauks.sebastian.footballacademies.model.news.NewsRepository
import java.util.*

class NewsViewModel(private val newsRepository: NewsRepository, private val storageDao: StorageDao): ViewModel() {

    fun getNewss() = newsRepository.getNewss()
    fun getUsers() = newsRepository.getUsers()

    fun startListening(chosenAcademyId: String, hideRefreshingIndicator: () -> Unit) = newsRepository.startListening(chosenAcademyId, hideRefreshingIndicator)

    fun addNews(authorId: String, title: String, content: String, creationDate: Date, imageName: String?, imageUrl:String?, videoName: String?, videoUrl:String?){
        newsRepository.addNews(authorId, title, content, creationDate, imageName, imageUrl, videoName, videoUrl)
    }
    fun removeNews(newsId: String, callback: () -> Unit) = newsRepository.removeNews(newsId, callback)
    fun uploadImage(name: String, data: ByteArray?, callback: (success:Boolean, name: String, fileUrl: String) -> Unit) = storageDao.uploadImage(name, data, callback)
    fun downloadImage(name: String, callback: (success: Boolean, data: ByteArray?) -> Unit) = storageDao.downloadImage(name, callback)
    fun downloadVideo(name: String, callback: (success: Boolean, data: ByteArray?) -> Unit) = storageDao.downloadVideo(name, callback)
    fun removeImage(name:String, callback: (success: Boolean) -> Unit) = storageDao.removeImage(name, callback)
    fun removeVideo(name:String, callback: (success: Boolean) -> Unit) = storageDao.removeVideo(name, callback)
    fun uploadVideo(name: String, uri: Uri,progressCallback: (progress: Double) -> Unit, callback: (success: Boolean, name:String, fileUrl: String) -> Unit) = storageDao.uploadVideo(name, uri,progressCallback, callback)
}