package lauks.sebastian.footballacademies.viewmodel.squad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import lauks.sebastian.footballacademies.model.squad.SquadRepository

class SquadViewModelFactory(private val squadRepository: SquadRepository):
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SquadViewModel(squadRepository) as T
    }
}