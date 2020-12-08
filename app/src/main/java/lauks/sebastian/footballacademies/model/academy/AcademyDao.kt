package lauks.sebastian.footballacademies.model.academy

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import lauks.sebastian.footballacademies.model.Player
import lauks.sebastian.footballacademies.utilities.UniqueCodeGenerator

class AcademyDao {
    private lateinit var academiesInFB: DatabaseReference
    private val academiesList = mutableListOf<Academy>()
    private val academies = MutableLiveData<List<Academy>>()
    private lateinit var loggedUserId: String

    init {
        academies.value = academiesList
    }

    fun startListening(loggedUserId: String, hideRefreshingIncdicator: () -> Unit) {
        this.loggedUserId = loggedUserId
        academiesList.clear()
        academiesInFB = Firebase.database.reference.child("academies")
        academiesInFB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                academiesList.clear()
                snapshot.children.forEach { academyFB: DataSnapshot ->
                    @Suppress("UNCHECKED_CAST")
                    val academyMap = academyFB.value as HashMap<String, *>
                    val academy = Academy(
                        academyMap["id"].toString(),
                        academyMap["name"].toString(),
                        academyMap["code"].toString(),
                        getPlayersIds(academyMap["players"])
                    )

                    if (academy.players.contains(loggedUserId)) academiesList.add(academy)

                }
                academies.value = academiesList
                hideRefreshingIncdicator()
            }

        })
    }

    fun fetchAcademy(academyId: String, callback: (academy: Academy) -> Unit){
        academiesInFB.orderByChild("id").equalTo(academyId).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.value != null){
                    snapshot.children.forEach {
                        @Suppress("UNCHECKED_CAST") val academyMap = it.value as HashMap<String, *>
                        val academy = Academy(
                            academyMap["id"].toString(),
                            academyMap["name"].toString(),
                            academyMap["code"].toString(),
                            getPlayersIds(academyMap["players"])
                        )
                        callback(academy)
                    }
                }
            }
        })
    }

    private fun getPlayersIds(players: Any?): MutableList<String> {
        val playersList = mutableListOf<String>()
        if (players != null) {
            @Suppress("UNCHECKED_CAST") val playersMap = players as HashMap<String, *>
            playersMap.forEach { (_, v) ->
                playersList.add(v as String)
            }
        }
        return playersList
    }

    fun addToSquad(academyCode: String, joinAcademyCallback: (joined: Boolean) -> Unit) {
        academiesInFB.orderByChild("code").equalTo(academyCode)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null) {
                        snapshot.children.forEach { child ->
                            val academyMap = child.value as HashMap<String, *>
                            val academyId = academyMap["id"] as String
                            val players = getPlayersIds(academyMap["players"])

                            if (players.size == 0 || !players.contains(loggedUserId)) {
                                val newPlayerKey =
                                    academiesInFB.child(academyId).child("players").push().key
                                academiesInFB.child(academyId).child("players")
                                    .child(newPlayerKey!!)
                                    .setValue(loggedUserId)
                                joinAcademyCallback(true)
                            }
                        }

                    }else{
                        joinAcademyCallback(false)
                    }
                }
            })
    }

    fun addAcademy(academyName: String,callback: ()-> Unit) {
        academiesInFB.orderByChild("name").equalTo(academyName)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value == null) {
                        val pushedRef = academiesInFB.push()
                        val pushedId = pushedRef.key
                        val players = mutableListOf<String>()
                        val academy = Academy(
                            pushedId!!,
                            academyName,
                            UniqueCodeGenerator.generateHash(10),
                            players
                        )
                        academiesInFB.child(academy.id).setValue(academy)
                        addToSquad(academy.code){
                            callback()
                        }
                    }
                }

            })
    }

    fun leaveAcademy(academyId: String, userId: String, refreshAcademies: () -> Unit) {
        academiesInFB.child(academyId).child("players").orderByValue().equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null) {
                        snapshot.children.forEach { child ->
                            academiesInFB.child(academyId).child("players").child(child.key!!)
                                .removeValue()
                            refreshAcademies()
                        }
                    }
                }
            })
    }

    fun generateNewCode(academyId: String, callback: () -> Unit){
        academiesInFB.orderByChild("id").equalTo(academyId).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.value != null){
                    snapshot.children.forEach { child->

                        @Suppress("UNCHECKED_CAST") val academyMap = child.value as HashMap<String, *>
                        val academyKey = child.key!!
                        val newCode = UniqueCodeGenerator.generateHash(10)

                        academiesInFB.child(academyKey).child("code").setValue(newCode)
                        callback()
                    }
                }

                        }
        })
    }

    fun getAcademies() = academies as LiveData<List<Academy>>
}