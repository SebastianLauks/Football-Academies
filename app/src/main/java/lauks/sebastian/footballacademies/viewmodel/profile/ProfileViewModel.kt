package lauks.sebastian.footballacademies.viewmodel.profile

import androidx.lifecycle.ViewModel
import lauks.sebastian.footballacademies.model.Player
import lauks.sebastian.footballacademies.model.StorageDao
import lauks.sebastian.footballacademies.model.profile.UserRepository

class ProfileViewModel(private val userRepository: UserRepository, private val storageDao: StorageDao): ViewModel() {

    fun fetchUser(userId: String, updateFields:(Player) -> Unit) = userRepository.fetchUser(userId, updateFields)
    fun setUserDetails(userId: String, firstname:String?, lastname: String?, height: Int?, weight: Int?, age:Int?, prefFoot: Int?,role: Int?, imageName: String?, imageUrl: String?, finishSetting: () -> Unit)
    =userRepository.setUserDetails(userId, firstname, lastname, height, weight, age, prefFoot,role, imageName, imageUrl, finishSetting)
    fun checkCredentials(login: String, password:String, callback: (logged:Boolean) -> Unit) = userRepository.checkCredentials(login, password, callback)
    fun signUpUser(login: String, password: String, callback: (logged: Boolean, message: String) -> Unit) = userRepository.signUpUser(login, password, callback)
    fun uploadImage(name: String, data: ByteArray?, callback: (success:Boolean, name: String, fileUrl: String) -> Unit) = storageDao.uploadImage(name, data, callback)
    fun removeImage(name:String, callback: (success: Boolean) -> Unit) = storageDao.removeImage(name, callback)

}