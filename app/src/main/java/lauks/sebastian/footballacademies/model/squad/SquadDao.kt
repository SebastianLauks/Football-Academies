package lauks.sebastian.footballacademies.model.squad

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import lauks.sebastian.footballacademies.model.Player

class SquadDao  {
    private lateinit var academiesInFB: DatabaseReference
    private lateinit var usersInFB: DatabaseReference

    private val playersList = mutableListOf<Player>()
    private val playersLiveData = MutableLiveData<List<Player>>()

    init{
        playersLiveData.value = playersList
    }


    fun fetchPlayers(academyId: String, hideRefreshingIndicator: () -> Unit) {
        playersList.clear()
        academiesInFB = Firebase.database.reference.child("academies")
        usersInFB = Firebase.database.reference.child("Users")

        academiesInFB.orderByChild("id").equalTo(academyId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null) {
                        snapshot.children.forEach { child ->
                            @Suppress("UNCHECKED_CAST") val academyMap = child.value as HashMap<String, *>
                            val playersIds = getPlayersIds(academyMap["players"])
                            playersIds.forEach { playerId ->
                                usersInFB.orderByChild("id").equalTo(playerId).addListenerForSingleValueEvent(object: ValueEventListener{
                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                    }

                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if(snapshot.value != null){
                                            snapshot.children.forEach { it ->
                                                @Suppress("UNCHECKED_CAST") val userMap = it.value as HashMap<String, *>
                                                val id = userMap.get("id").toString()
                                                val firstname = userMap.get("firstname").toString()
                                                val lastname = userMap.get("lastname")?.toString()
                                                val height = userMap.get("height")?.toString()?.toInt()
                                                val age = userMap.get("age")?.toString()?.toInt()
                                                val weight = userMap.get("weight")?.toString()?.toInt()
                                                val prefFoot = userMap.get("prefFoot")?.toString()?.toInt()

                                                val player = Player(id, firstname, lastname, height, weight, age, prefFoot)
                                                playersList.add(player)
                                            }
                                        }
                                        playersLiveData.value = playersList
                                        hideRefreshingIndicator()
                                    }
                                })
                            }
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

    fun getPlayers() = playersLiveData as LiveData<List<Player>>

}