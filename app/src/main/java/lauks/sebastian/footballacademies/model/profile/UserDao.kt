package lauks.sebastian.footballacademies.model.profile

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import lauks.sebastian.footballacademies.model.Player
import kotlin.math.log

class UserDao {
    private lateinit var usersInFB: DatabaseReference


    fun setUserDetails(
        userId: String,
        firstname: String?,
        lastname: String?,
        height: Int?,
        weight: Int?,
        age: Int?,
        prefFoot: Int?,
        role: Int?,
        imageName: String?,
        imageUrl: String?,
        finishSetting: () -> Unit
    ) {
        usersInFB = Firebase.database.reference.child("Users")

        usersInFB.orderByChild("id").equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null) {
                        snapshot.children.forEach { child ->
                            val objId = child.key!!

                            usersInFB.child(objId).child("firstname").setValue(firstname)
                            usersInFB.child(objId).child("lastname").setValue(lastname)
                            usersInFB.child(objId).child("height").setValue(height)
                            usersInFB.child(objId).child("weight").setValue(weight)
                            usersInFB.child(objId).child("age").setValue(age)
                            usersInFB.child(objId).child("prefFoot").setValue(prefFoot)
                            usersInFB.child(objId).child("role").setValue(role)
                            usersInFB.child(objId).child("imageName").setValue(imageName)
                            usersInFB.child(objId).child("imageUrl").setValue(imageUrl)

                            finishSetting()
                        }
                    }


                }
            })
    }

    fun fetchUser(userId: String, updateFields: (Player) -> Unit) {
        usersInFB = Firebase.database.reference.child("Users")

        usersInFB.orderByChild("id").equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null) {
                        snapshot.children.forEach { child ->
                            @Suppress("UNCHECKED_CAST") val userMap =
                                child.value as HashMap<String, *>
                            val id = userMap["id"].toString()
                            val firstname = userMap["firstname"]?.toString()
                            val lastname = userMap["lastname"]?.toString()
                            val height = userMap["height"]?.toString()?.toInt()
                            val weight = userMap["weight"]?.toString()?.toInt()
                            val age = userMap["age"]?.toString()?.toInt()
                            val prefFoot = userMap["prefFoot"]?.toString()?.toInt()
                            val role = userMap["role"]?.toString()?.toInt()
                            val imageName = userMap["imageName"]?.toString()
                            val imageUrl = userMap["imageUrl"]?.toString()

                            val player =
                                Player(id, firstname, lastname, height, weight, age, prefFoot, role,imageName, imageUrl)
                            updateFields(player)
                        }
                    }


                }
            })
    }

    fun checkCredentials(login: String, password: String, callback: (logged: Boolean) -> Unit) {
        usersInFB = Firebase.database.reference.child("Users")

        usersInFB.orderByChild("id").equalTo(login)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null) {
                        snapshot.children.forEach { child ->
                            @Suppress("UNCHECKED_CAST") val userMap =
                                child.value as HashMap<String, *>
                            val passwordDB = userMap["password"]?.toString()
                            if (password == passwordDB) {
                                callback(true)
                            } else {
                                callback(false)
                            }
                        }
                    } else {
                        callback(false)
                    }
                }
            })
    }

    fun signUpUser(
        login: String,
        password: String,
        callback: (logged: Boolean, message: String) -> Unit
    ) {
        usersInFB = Firebase.database.reference.child("Users")

        usersInFB.orderByChild("id").equalTo(login)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null) {
                        callback(false, "Podana nazwa użytkownika jest zajęta.")
                    } else {
                        val user = User(login, password, null, null)
                        val pushedRef = usersInFB.push()
                        usersInFB.child(pushedRef.key!!).setValue(user)
                        callback(true, "")
                    }
                }
            })
    }
}