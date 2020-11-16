package lauks.sebastian.footballacademies.model.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import lauks.sebastian.footballacademies.model.User
import java.util.*

class NewsDao {
    private lateinit var newsInFB: DatabaseReference
    private lateinit var academiesInFB: DatabaseReference
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

    fun startListening(academyKey: String){
        newsInFB = Firebase.database.reference.child("News")
        academiesInFB = Firebase.database.reference.child("Academies")

    }

    fun getNewss() = newsLiveData as LiveData<List<News>>

}