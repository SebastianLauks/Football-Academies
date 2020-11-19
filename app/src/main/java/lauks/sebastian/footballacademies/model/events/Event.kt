package lauks.sebastian.footballacademies.model.events

import lauks.sebastian.footballacademies.model.User
import java.util.*

data class Event(val id: String, var type: String, var date: Long, var place: String, var notes: String, val confirmedParticipants: List<User>, val unconfirmedParticipants: List<User>) {
}