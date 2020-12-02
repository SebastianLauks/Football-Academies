package lauks.sebastian.footballacademies.model.news

data class News(var id: String="-1", val authorId: String,var academyId: String="-1", var title: String, var content: String, val creationDate: Long, val imageName: String?, val imageUrl: String?) {

}