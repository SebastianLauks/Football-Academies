package lauks.sebastian.footballacademies.model.events

import android.text.BoringLayout
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.util.Strings
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import lauks.sebastian.footballacademies.model.Player
import lauks.sebastian.footballacademies.model.User
import java.util.*
import kotlin.collections.HashMap

class EventsDao {

    private lateinit var eventsInFB: DatabaseReference
    private lateinit var usersInFB: DatabaseReference
    private lateinit var academiesInFB: DatabaseReference
    private lateinit var academyKey: String
    private lateinit var loggedUserId: String
    private val eventsList = mutableListOf<Event>()
    private val eventsLiveData = MutableLiveData<List<Event>>()

    private val confirmedUsersList = mutableListOf<Player>()
    private val confirmedUsersLiveData = MutableLiveData<List<Player>>()
    private val allUsers = mutableListOf<Player>()
    private val allUsersLiveData = MutableLiveData<List<Player>>()

    private var matches: Boolean = true
    private var tournaments: Boolean = true
    private var trainings: Boolean = true

    init {
//        val event1 = Event("001", "Mecz", Date().time, "Wrocław", "Notatki", mutableListOf(),
//            )
//        eventsList.add(event1)
        eventsLiveData.value = eventsList
        allUsersLiveData.value = allUsers
        confirmedUsersLiveData.value = confirmedUsersList
    }

    fun startListening(
        academyKey: String,
        loggedUserId: String,
        hideRefreshingIndicator: () -> Unit
    ) {
        this.loggedUserId = loggedUserId
        this.academyKey = academyKey
        eventsInFB = Firebase.database.reference.child("Events")

        eventsList.clear()


        getUserEventsFilters { matches, tournaments, trainings ->
            this.matches = matches
            this.tournaments = tournaments
            this.trainings = trainings
        }
        eventsInFB.orderByChild("academyId").equalTo(academyKey)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    eventsList.clear()

                    if (snapshot.value != null) {
                        snapshot.children.forEach { child ->
                            @Suppress("UNCHECKED_CAST") val eventMap =
                                child.value as HashMap<String, *>
                            val type: String = eventMap["type"].toString()
                            var shouldAppear = false
                            when (type) {
                                "Turniej" -> if (tournaments) shouldAppear = true
                                "Mecz" -> if (matches) shouldAppear = true
                                "Trening" -> if (trainings) shouldAppear = true
                                else -> shouldAppear = false
                            }
                            if(shouldAppear) {
                                val id: String = eventMap["id"].toString()
                                val authorId: String = eventMap["authorId"].toString()
                                val academyId: String = eventMap["academyId"].toString()
                                val date: Long = eventMap["date"].toString().toLong()
                                val place: String = eventMap["place"].toString()
                                val notes: String = eventMap["notes"].toString()
                                val confirmedParticipants =
                                    getConfirmedParticipants(eventMap["confirmedParticipants"])


                                val event = Event(
                                    id,
                                    authorId,
                                    academyId,
                                    type,
                                    date,
                                    place,
                                    notes,
                                    confirmedParticipants
                                ) // ToDo properly download list of users
                                eventsList.add(event)
                            }
                        }
                    }
                    eventsList.sortByDescending { it.date }
                    hideRefreshingIndicator()
                    eventsLiveData.value = eventsList
                }

            })
    }


    fun setUserEventsFilters(matches: Boolean, tournaments: Boolean, trainings: Boolean, finish: () -> Unit) {
        usersInFB = Firebase.database.reference.child("Users")

        usersInFB.orderByChild("id").equalTo(loggedUserId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null) {
                        snapshot.children.forEach { child ->
                            val userKey = child.key!!
                            usersInFB.child(userKey).child("eventsFilters").child("matches")
                                .setValue(matches)
                            usersInFB.child(userKey).child("eventsFilters").child("tournaments")
                                .setValue(tournaments)
                            usersInFB.child(userKey).child("eventsFilters").child("trainings")
                                .setValue(trainings)

                            finish()
                        }
                    }
                }

            })
    }


    fun fetchConfirmedParticipants(userIds: List<String>, callback: () -> Unit ){
        usersInFB = Firebase.database.reference.child("Users")

        var itemsProcessed = 0
        confirmedUsersList.clear()
        allUsers.clear()
        userIds.forEach {
            usersInFB.orderByChild("id").equalTo(it).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.value != null){
                        snapshot.children.forEach {
                            @Suppress("UNCHECKED_CAST") val userMap = it.value as HashMap<String, *>
                            val id = userMap.get("id").toString()
                            val firstname = userMap.get("firstname").toString()
                            val lastname = userMap.get("lastname")?.toString()
                            val height = userMap.get("height")?.toString()?.toInt()
                            val age = userMap.get("age")?.toString()?.toInt()
                            val weight = userMap.get("weight")?.toString()?.toInt()
                            val prefFoot = userMap.get("prefFoot")?.toString()?.toInt()

                            val player = Player(id, firstname, lastname, height, weight, age, prefFoot)
                            confirmedUsersList.add(player)
                            if(++itemsProcessed == userIds.size){
                                confirmedUsersLiveData.value = confirmedUsersList
                                callback()
                            }
                        }
                    }
                }
            })

        }
    }

    fun getAllUsers() = allUsersLiveData as LiveData<List<Player>>
    fun getConfirmedUsers() = confirmedUsersLiveData as LiveData<List<Player>>

    fun fetchAllUsers(callback: () -> Unit){
        academiesInFB = Firebase.database.reference.child("academies")
        academiesInFB.orderByChild("id").equalTo(academyKey)
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
                                                allUsers.add(player)
                                            }
                                        }
                                        allUsersLiveData.value = allUsers
                                        callback()
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


    fun getUserEventsFilters(updateEventsFiltersCheckboxes: (Boolean, Boolean, Boolean) -> Unit) {
        usersInFB = Firebase.database.reference.child("Users")

        usersInFB.orderByChild("id").equalTo(loggedUserId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null) {
                        snapshot.children.forEach { child ->
                            @Suppress("UNCHECKED_CAST") val userMap =
                                child.value as HashMap<String, *>
                            val eventFiltersAny = userMap["eventsFilters"]
                            if (eventFiltersAny == null) {
                                updateEventsFiltersCheckboxes(true, true, true)
                            } else {
                                @Suppress("UNCHECKED_CAST") val eventsFiltersMap =
                                    eventFiltersAny as HashMap<String, *>
                                val matches = eventsFiltersMap["matches"].toString().toBoolean()
                                val tournaments =
                                    eventsFiltersMap["tournaments"].toString().toBoolean()
                                val trainings = eventsFiltersMap["trainings"].toString().toBoolean()
                                updateEventsFiltersCheckboxes(matches, tournaments, trainings)

                            }
                        }
                    }
                }

            })
    }

    private fun getConfirmedParticipants(confParticip: Any?): MutableList<String> {
        val confirmedParticipants = mutableListOf<String>()
        if (confParticip != null) {
            @Suppress("UNCHECKED_CAST") val confirmedParticipantsMap =
                confParticip as HashMap<String, *>
            confirmedParticipantsMap.forEach { (_, v) ->
                confirmedParticipants.add(v as String)
            }
        }
        return confirmedParticipants
    }

    fun changePresence(eventId: String, presence: Boolean) {
        eventsInFB.orderByChild("id").equalTo(eventId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null) {
                        @Suppress("UNCHECKED_CAST") val eventMap =
                            snapshot.child(eventId).value as HashMap<String, *>


                        if (eventMap["confirmedParticipants"] != null) {
                            val confirmedParticipants =
                                getConfirmedParticipants(eventMap["confirmedParticipants"])
                            if (presence) {
                                if (!confirmedParticipants.contains(loggedUserId)) {
                                    val ref =
                                        eventsInFB.child(eventId).child("confirmedParticipants")
                                            .push()
                                    val refKey = ref.key
                                    eventsInFB.child(eventId).child("confirmedParticipants")
                                        .child(refKey!!).setValue(loggedUserId)
                                }
                            } else {
                                eventsInFB.child(eventId).child("confirmedParticipants")
                                    .orderByValue().equalTo(loggedUserId)
                                    .addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onCancelled(error: DatabaseError) {
                                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                        }

                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            if (snapshot.value != null) {
                                                @Suppress("UNCHECKED_CAST") val snapMap =
                                                    snapshot.value as HashMap<String, *>
                                                snapMap.keys.forEach { k ->
                                                    eventsInFB.child(eventId)
                                                        .child("confirmedParticipants").child(k)
                                                        .removeValue()
                                                }

                                            }

                                        }
                                    })

                            }
                        } else {
//                            val confirmedParticipants = mutableListOf<String>()
//                            confirmedParticipants.add(loggedUserId)
                            if (presence) {
                                val ref =
                                    eventsInFB.child(eventId).child("confirmedParticipants").push()
                                val refKey = ref.key
                                eventsInFB.child(eventId).child("confirmedParticipants")
                                    .child(refKey!!).setValue(loggedUserId)
                            }
                        }

//                        confirmedParticipants.set(loggedUserId, presence)


                    }
                }

            })
    }


    fun addEvent(authorId: String, type: String, date: Long, place: String, notes: String) {
        eventsInFB = Firebase.database.reference.child("Events")
        val pushedEventRef = eventsInFB.push()
        val pushedEventId = pushedEventRef.key

        val event =
            Event(pushedEventId!!, authorId, academyKey, type, date, place, notes, listOf())

        eventsInFB.child(event.id).setValue(event)
    }

    fun removeEvent(eventId: String){
        eventsInFB.child(eventId).removeValue()
    }

    fun getEvents() = eventsLiveData as LiveData<List<Event>>

}