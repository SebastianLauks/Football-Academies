package lauks.sebastian.footballacademies.model.news

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import lauks.sebastian.footballacademies.model.User
import java.util.*
import kotlin.collections.HashMap

class NewsDao {
    private lateinit var newsInFB: DatabaseReference
    private lateinit var academiesInFB: DatabaseReference
    private val newsList = mutableListOf<News>()
    private val newsLiveData = MutableLiveData<List<News>>()
    private lateinit var academyKey: String


    init {
//        val user1 = User("id01", "Adam", "Kowalski")
//        val news1 = News("id01",user1.id,"Tytul-2", "To jest drugi przykładowy opis posta", Date())
//        val user2 = User("id02", "Robert", "Lewandowski")
//        val news2 = News("id02",user2.id,"Tytul-1", "To jest przykładowy opis posta", Date())
//        newsList.add(news1)
//        newsList.add(news2)
        newsLiveData.value = newsList
    }

    fun startListening(academyKey: String, hideRefreshingIndicator: () -> Unit){
        newsList.clear()
        this.academyKey = academyKey
        newsInFB = Firebase.database.reference.child("News")
//        academiesInFB = Firebase.database.reference.child("Academies")

        newsInFB.orderByChild("academyId").equalTo(academyKey).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.value != null){

                    snapshot.children.forEach { child ->
                        @Suppress("UNCHECKED_CAST") val newsMap = child.value as HashMap<String, *>
                        val id = newsMap["id"].toString()
                        val academyId = newsMap["academyId"].toString()

                        @Suppress("UNCHECKED_CAST") val userMap = newsMap["author"] as HashMap<String, *>
                        val authorId = userMap["id"].toString()
                        val firstname = userMap["firstname"].toString()
                        val lastname = userMap["lastname"].toString()
                        val author = User(authorId,firstname, lastname)


                        val creationDate = newsMap["creationDate"].toString().toLong()
                        val title = newsMap["title"].toString()
                        val content = newsMap["content"].toString()

                        val news = News(id, author, academyId, title, content, creationDate)

                        newsList.add(news)
                    }
                }
                newsList.sortByDescending { it.creationDate }
                newsLiveData.value = newsList
                hideRefreshingIndicator()
            }

        })

    }

    fun addNews(authorId: String, title: String, content: String, creationDate: Date){
        val pushedNewsRef = newsInFB.push()
        val pushedNewsId = pushedNewsRef.key

        //HERE DOWNLOAD USER OF ID authorID
        val news = News(pushedNewsId!!, User(authorId, "F","LastName"),academyKey, title, content, creationDate.time)
//        news.id = pushedNewsId!!
//        news.academyId = academyKey
        newsInFB.child(news.id).setValue(news)
    }

    fun getNewss() = newsLiveData as LiveData<List<News>>

}