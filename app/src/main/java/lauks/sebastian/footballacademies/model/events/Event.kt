package lauks.sebastian.footballacademies.model.events

import lauks.sebastian.footballacademies.model.User
import java.io.Serializable
import java.util.*

data class Event(val id: String, val authorId: String,val academyId: String, var type: String, var date: Long, var place: String, var notes: String, val confirmedParticipants: List<String>): Serializable {
}