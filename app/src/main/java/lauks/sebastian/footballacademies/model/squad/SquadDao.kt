package lauks.sebastian.footballacademies.model.squad

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import lauks.sebastian.footballacademies.model.Player

class SquadDao  {
    private val playersList = mutableListOf<Player>()
    private val playersLiveData = MutableLiveData<List<Player>>()

    init{
        val player1 = Player("id001", "Adam", "Błaszczykowski", 160, 50, 14, 0)
        val player2 = Player("id002", "Karol", "Piszczek", 155, 43, 13, 2)
        playersList.add(player1)
        playersList.add(player2)
        playersLiveData.value = playersList
    }


    fun getPlayers() = playersLiveData as LiveData<List<Player>>

}