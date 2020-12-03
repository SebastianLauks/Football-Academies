package lauks.sebastian.footballacademies.viewmodel.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import lauks.sebastian.footballacademies.model.StorageDao
import lauks.sebastian.footballacademies.model.profile.UserRepository

class ProfileViewModelFactory(private val userRepository: UserRepository, private val storageDao: StorageDao):ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ProfileViewModel(userRepository, storageDao) as T
    }


}