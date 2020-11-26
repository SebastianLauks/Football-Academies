package lauks.sebastian.footballacademies.viewmodel.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import lauks.sebastian.footballacademies.model.profile.UserRepository

class EditProfileViewModelFactory(private val userRepository: UserRepository):ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return EditProfileViewModel(userRepository) as T
    }


}