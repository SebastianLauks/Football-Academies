package lauks.sebastian.footballacademies.viewmodel.academies

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import lauks.sebastian.footballacademies.model.academy.Academy
import lauks.sebastian.footballacademies.model.academy.AcademyRepository

class AcademiesViewModel(private val academyRepository: AcademyRepository) : ViewModel() {


    fun leaveAcademy(academyId: String, userId: String, refreshAcademies: () -> Unit) = academyRepository.leaveAcademy(academyId, userId, refreshAcademies)
    fun getAcademies() = academyRepository.getAcademies()
    fun addAcademy(academyName: String) = academyRepository.addAcademy(academyName)
    fun addToSquad(academyCode: String) = academyRepository.addToSquad(academyCode)
    fun startListening(loggedUserId: String, hideRefreshingIncdicator: () -> Unit) = academyRepository.startListening(loggedUserId, hideRefreshingIncdicator)

}