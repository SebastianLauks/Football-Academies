package lauks.sebastian.footballacademies.viewmodel.academies

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import lauks.sebastian.footballacademies.model.academy.Academy
import lauks.sebastian.footballacademies.model.academy.AcademyRepository

class AcademiesViewModel(private val academyRepository: AcademyRepository) : ViewModel() {


    fun leaveAcademy(academyId: String, userId: String, refreshAcademies: () -> Unit) = academyRepository.leaveAcademy(academyId, userId, refreshAcademies)
    fun getAcademies() = academyRepository.getAcademies()
    fun addAcademy(academyName: String, callback: ()-> Unit) = academyRepository.addAcademy(academyName, callback)
    fun addToSquad(academyCode: String, joinAcademyCallback: (joined: Boolean) -> Unit) = academyRepository.addToSquad(academyCode, joinAcademyCallback)
    fun startListening(loggedUserId: String, hideRefreshingIncdicator: () -> Unit) = academyRepository.startListening(loggedUserId, hideRefreshingIncdicator)
    fun fetchAcademy(academyId: String, callback: (academy: Academy) -> Unit) = academyRepository.fetchAcademy(academyId, callback)

}