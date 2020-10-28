package lauks.sebastian.footballacademies.model.academy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import lauks.sebastian.footballacademies.model.academy.Academy

class AcademyDao {
    private val academiesList = mutableListOf<Academy>()
    private val academies = MutableLiveData<List<Academy>>()

    init {
        academiesList.add(Academy("1", "Akademia Pierwsza"))
        academiesList.add(Academy("2", "Akademia Druga"))
        academiesList.add(Academy("3", "Akademia Trzecia"))
        academies.value = academiesList
    }

    fun addAcademy(academy: Academy){
        //ToDo
    }

    fun getAcademies() = academies as LiveData<List<Academy>>
}