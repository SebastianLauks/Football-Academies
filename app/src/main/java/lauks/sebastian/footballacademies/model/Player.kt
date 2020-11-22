package lauks.sebastian.footballacademies.model

data class Player (val id: String, val firstname: String?, val lastname: String?, var height: Int?, var weight: Int?, var age: Int?, var prefFoot: Int?) {

    //prefFoot - 0-right, 1-left, 2-both

}