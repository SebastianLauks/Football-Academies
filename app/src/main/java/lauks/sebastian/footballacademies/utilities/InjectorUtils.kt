package lauks.sebastian.footballacademies.utilities

import android.provider.ContactsContract
import lauks.sebastian.footballacademies.model.Database
import lauks.sebastian.footballacademies.model.academy.AcademyRepository
import lauks.sebastian.footballacademies.model.news.NewsRepository
import lauks.sebastian.footballacademies.model.squad.SquadRepository
import lauks.sebastian.footballacademies.viewmodel.academies.AcademiesViewModelFactory
import lauks.sebastian.footballacademies.viewmodel.news.NewsViewModelFactory
import lauks.sebastian.footballacademies.viewmodel.squad.SquadViewModelFactory

object InjectorUtils {
    fun provideAcademiesViewModelFactory(): AcademiesViewModelFactory {
        // The whole dependency tree is constructed right here, in one place
        val academyRepository = AcademyRepository.getInstance(Database.getInstance().academyDao)
        return AcademiesViewModelFactory(
            academyRepository
        )
    }

    fun provideNewsViewModelFactory(): NewsViewModelFactory{
        val newsRepository = NewsRepository.getInstance(Database.getInstance().newsDao)
        return NewsViewModelFactory(newsRepository)
    }


    fun provideSquadViewModelFactory(): SquadViewModelFactory{
        val squadRepository= SquadRepository.getInstance(Database.getInstance().squadDao)
        return SquadViewModelFactory(squadRepository)
    }

}