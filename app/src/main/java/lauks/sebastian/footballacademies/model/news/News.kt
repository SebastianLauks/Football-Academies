package lauks.sebastian.footballacademies.model.news

import lauks.sebastian.footballacademies.model.User
import java.util.*

data class News(val id: String, val author: User, var title: String, var content: String, val creationDate: Date) {

}