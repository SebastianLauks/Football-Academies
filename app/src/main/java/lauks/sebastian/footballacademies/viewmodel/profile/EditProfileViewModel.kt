package lauks.sebastian.footballacademies.viewmodel.profile

import androidx.lifecycle.ViewModel
import lauks.sebastian.footballacademies.model.Player
import lauks.sebastian.footballacademies.model.profile.UserRepository

class EditProfileViewModel(private val userRepository: UserRepository): ViewModel() {

    fun fetchUser(userId: String, updateFields:(Player) -> Unit) = userRepository.fetchUser(userId, updateFields)
    fun setUserDetails(userId: String, firstname:String?, lastname: String?, height: Int?, weight: Int?, age:Int?, prefFoot: Int?, finishSetting: () -> Unit)
    =userRepository.setUserDetails(userId, firstname, lastname, height, weight, age, prefFoot, finishSetting)
}