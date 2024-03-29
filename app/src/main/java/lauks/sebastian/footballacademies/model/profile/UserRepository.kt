package lauks.sebastian.footballacademies.model.profile

import lauks.sebastian.footballacademies.model.Player

class UserRepository private constructor(private val userDao: UserDao){

    fun fetchUser(userId: String, updateFields:(Player) -> Unit) = userDao.fetchUser(userId, updateFields)
    fun setUserDetails(userId: String, firstname:String?, lastname: String?, height: Int?, weight: Int?, age:Int?, prefFoot: Int?,role: Int?, imageName: String?, imageUrl: String?, finishSetting: () -> Unit)
    = userDao.setUserDetails(userId, firstname, lastname, height, weight, age, prefFoot,role,imageName,imageUrl, finishSetting)
    fun checkCredentials(login: String, password:String, callback: (logged:Boolean) -> Unit) = userDao.checkCredentials(login, password, callback)
    fun signUpUser(login: String, password: String, callback: (logged: Boolean, message: String) -> Unit) = userDao.signUpUser(login, password, callback)

    companion object{
        // Singleton instantiation you already know and love
        @Volatile private var instance: UserRepository? = null

        fun getInstance(squadDao: UserDao) =
            instance ?: synchronized(this) {
                instance ?: UserRepository(squadDao).also { instance = it }
            }
    }
}