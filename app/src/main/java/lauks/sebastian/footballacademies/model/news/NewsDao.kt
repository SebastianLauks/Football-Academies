package lauks.sebastian.footballacademies.model.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import lauks.sebastian.footballacademies.model.User
import java.util.*

class NewsDao {
    private val newsList = mutableListOf<News>()
    private val newsLiveData = MutableLiveData<List<News>>()

    init {
        val user1 = User("id01", "Adam", "Kowalski")
        val news1 = News("id01",user1,"Tytul-2", "To jest drugi przykładowy opis posta", Date())
        val user2 = User("id02", "Robert", "Lewandowski")
        val news2 = News("id02",user2,"Tytul-1", "To jest przykładowy opis posta", Date())
        newsList.add(news1)
        newsList.add(news2)
        newsLiveData.value = newsList
    }

    fun getNewss() = newsLiveData as LiveData<List<News>>

}