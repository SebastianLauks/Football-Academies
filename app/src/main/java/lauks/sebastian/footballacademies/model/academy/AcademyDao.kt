package lauks.sebastian.footballacademies.model.academy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import lauks.sebastian.footballacademies.model.academy.Academy

class AcademyDao {
    private lateinit var academiesInFB: DatabaseReference
    private val academiesList = mutableListOf<Academy>()
    private val academies = MutableLiveData<List<Academy>>()

    init {
        academies.value = academiesList
    }

    fun startListening() {
        academiesInFB = Firebase.database.reference.child("academies")
        academiesInFB.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                academiesList.clear()

                snapshot.children.forEach{academyFB: DataSnapshot ->
                    @Suppress("UNCHECKED_CAST")
                    val academyMap = academyFB.value as HashMap<String, *>
                    val academy = Academy(
                        academyMap["id"].toString(),
                        academyMap["name"].toString()
                    )
                    academiesList.add(academy)

                }
                academies.value = academiesList
            }

        })
    }


    fun addAcademy(academy: Academy) {
        academiesInFB.orderByChild("name").equalTo(academy.name)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.value == null){
                        val pushedRef = academiesInFB.push()
                        val pushedId = pushedRef.key
                        academy.id = pushedId!!
                        academiesInFB.child(academy.id).setValue(academy)
                    }
                }

            })
    }

    fun getAcademies() = academies as LiveData<List<Academy>>
}