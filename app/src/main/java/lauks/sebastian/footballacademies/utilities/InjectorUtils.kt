package lauks.sebastian.footballacademies.utilities

import lauks.sebastian.footballacademies.model.Database
import lauks.sebastian.footballacademies.model.academy.AcademyRepository
import lauks.sebastian.footballacademies.viewmodel.AcademiesViewModelFactory

object InjectorUtils {
    fun provideAcademiesViewModelFactory(): AcademiesViewModelFactory {

        // The whole dependency tree is constructed right here, in one place
        val academyRepository = AcademyRepository.getInstance(Database.getInstance().academyDao)
        return AcademiesViewModelFactory(academyRepository)
    }
}