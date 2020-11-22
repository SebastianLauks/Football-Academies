package lauks.sebastian.footballacademies.viewmodel.squad

import androidx.lifecycle.ViewModel
import lauks.sebastian.footballacademies.model.squad.SquadRepository

class SquadViewModel(private val squadRepository: SquadRepository): ViewModel() {

    fun fetchPlayers(academyId: String, hideRefreshingIndicator: () -> Unit) = squadRepository.fetchPlayers(academyId, hideRefreshingIndicator)
    fun getPlayers() = squadRepository.getPlayers()
}