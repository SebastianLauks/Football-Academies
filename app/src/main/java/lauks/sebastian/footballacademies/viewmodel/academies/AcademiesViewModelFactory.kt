package lauks.sebastian.footballacademies.viewmodel.academies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import lauks.sebastian.footballacademies.model.academy.AcademyRepository

class AcademiesViewModelFactory(private val academyRepository: AcademyRepository): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AcademiesViewModel(
            academyRepository
        ) as T
    }
}