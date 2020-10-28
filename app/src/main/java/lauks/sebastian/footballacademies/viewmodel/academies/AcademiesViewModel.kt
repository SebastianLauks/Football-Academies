package lauks.sebastian.footballacademies.viewmodel.academies

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import lauks.sebastian.footballacademies.model.academy.Academy
import lauks.sebastian.footballacademies.model.academy.AcademyRepository

class AcademiesViewModel(private val academyRepository: AcademyRepository) : ViewModel() {

    fun getAcademies() = academyRepository.getAcademies()
    fun addAcademy(academy: Academy) = academyRepository.addAcademy(academy)

}