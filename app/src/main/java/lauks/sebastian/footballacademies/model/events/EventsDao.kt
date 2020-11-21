package lauks.sebastian.footballacademies.model.events

import android.text.BoringLayout
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.util.Strings
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import lauks.sebastian.footballacademies.model.User
import java.util.*
import kotlin.collections.HashMap

class EventsDao {

    private lateinit var eventsInFB: DatabaseReference
    private lateinit var academyKey: String
    private lateinit var loggedUserId: String
    private val eventsList = mutableListOf<Event>()
    private val eventsLiveData = MutableLiveData<List<Event>>()
    private var observingAcademy: String = "any"

    init {
//        val event1 = Event("001", "Mecz", Date().time, "Wrocław", "Notatki", mutableListOf(),
//            )
//        eventsList.add(event1)
        eventsLiveData.value = eventsList
    }

    fun startListening(academyKey: String, loggedUserId: String, hideRefreshingIndicator: () -> Unit) {
        this.loggedUserId = loggedUserId
        this.academyKey = academyKey
        eventsInFB = Firebase.database.reference.child("Events")

        eventsList.clear()
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
                            val id: String = eventMap["id"].toString()
                            val authorId: String = eventMap["authorId"].toString()
                            val academyId: String = eventMap["academyId"].toString()
                            val type: String = eventMap["type"].toString()
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
                    eventsList.sortByDescending { it.date }
                    hideRefreshingIndicator()
                    eventsLiveData.value = eventsList
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

    fun getEvents() = eventsLiveData as LiveData<List<Event>>

}