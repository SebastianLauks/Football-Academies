package lauks.sebastian.footballacademies.utilities

import lauks.sebastian.footballacademies.model.Database
import lauks.sebastian.footballacademies.model.StorageDao
import lauks.sebastian.footballacademies.model.academy.AcademyRepository
import lauks.sebastian.footballacademies.model.events.EventRepository
import lauks.sebastian.footballacademies.model.news.NewsRepository
import lauks.sebastian.footballacademies.model.profile.UserRepository
import lauks.sebastian.footballacademies.model.squad.SquadRepository
import lauks.sebastian.footballacademies.viewmodel.academies.AcademiesViewModelFactory
import lauks.sebastian.footballacademies.viewmodel.events.EventsViewModelFactory
import lauks.sebastian.footballacademies.viewmodel.news.NewsViewModelFactory
import lauks.sebastian.footballacademies.viewmodel.profile.ProfileViewModelFactory
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
        val storageDao = StorageDao.getInstance()
        return NewsViewModelFactory(newsRepository, storageDao )
    }


    fun provideSquadViewModelFactory(): SquadViewModelFactory{
        val squadRepository= SquadRepository.getInstance(Database.getInstance().squadDao)
        return SquadViewModelFactory(squadRepository)
    }

    fun provideEventsViewModelFactory(): EventsViewModelFactory{
        val eventRepository = EventRepository.getInstance(Database.getInstance().eventsDao)
        return EventsViewModelFactory(eventRepository)
    }

    fun provideProfileViewModelFactory(): ProfileViewModelFactory{
        val userRepository = UserRepository.getInstance(Database.getInstance().userDao)
        return ProfileViewModelFactory(userRepository)
    }

}