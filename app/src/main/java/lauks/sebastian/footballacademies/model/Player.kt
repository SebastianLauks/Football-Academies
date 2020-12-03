package lauks.sebastian.footballacademies.model

data class Player (val id: String, var firstname: String?, var lastname: String?, var height: Int?, var weight: Int?, var age: Int?, var prefFoot: Int?, var role: Int?, var imageName: String?="", var imageUrl: String?="") {

    //prefFoot - 0-dontKnow 1-right, 2-left, 3-both
    //role - 0-dontKnow 1-player, 2-parent, 3-employer

}