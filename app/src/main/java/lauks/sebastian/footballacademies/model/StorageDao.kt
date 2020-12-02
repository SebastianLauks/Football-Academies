package lauks.sebastian.footballacademies.model

import android.net.Uri
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class StorageDao private constructor(){

    private val storageRef: StorageReference = FirebaseStorage.getInstance().reference


    fun uploadVideo(name: String, uri: Uri,progressCallback: (progress: Double) -> Unit, callback: (success: Boolean, name:String, fileUrl: String) -> Unit){
        storageRef.child("videos/$name").putFile(uri).addOnSuccessListener {task ->
            storageRef.child("videos/$name").downloadUrl.addOnCompleteListener {
                if(it.isSuccessful){
                    callback(true, task.metadata!!.name.toString(), it.result.toString())
                }
            }
        }.addOnProgressListener {task ->
             val progress = 100 * (task.bytesTransferred.toDouble() / task.totalByteCount.toDouble())
            progressCallback(progress)
        }
    }

    fun uploadImage(name: String, data: ByteArray?, callback: (success:Boolean, name: String, fileUrl: String) -> Unit){

        if(data == null)
            callback(false, "", "")
        else{
            storageRef.child("images/$name").putBytes(data).addOnFailureListener {
                Log.d("exc", it.toString())
                callback(false, "", "")
            }.addOnSuccessListener { taskSnapshot ->
                Log.d("metadata", taskSnapshot.metadata!!.name.toString())
                storageRef.child("images/$name").downloadUrl.addOnCompleteListener{
                    if(it.isSuccessful){
                        callback(true,  taskSnapshot.metadata!!.name.toString(), it.result.toString())
                    }
                }

            }
        }
    }

    fun removeImage(name:String, callback: (success: Boolean) -> Unit){
        storageRef.child("images/$name").delete().addOnSuccessListener {
            callback(true)
        }.addOnFailureListener {
            callback(false)
        }
    }

    fun removeVideo(name:String, callback: (success: Boolean) -> Unit){
        storageRef.child("videos/$name").delete().addOnSuccessListener {
            callback(true)
        }.addOnFailureListener {
            callback(false)
        }
    }

    fun downloadVideo(name: String, callback: (success: Boolean, data: ByteArray?) -> Unit){
        val MAX_SIZE: Long = 1024 * 1024 * 100
        storageRef.child("videos/$name").getBytes(MAX_SIZE).addOnSuccessListener {
            callback(true, it)
        }.addOnFailureListener {
            callback(false, null)
        }

    }

    fun downloadImage(name: String, callback: (success: Boolean, data: ByteArray?) -> Unit){
        val MAX_SIZE: Long = 1024 * 1024 * 5
        storageRef.child("images/$name").getBytes(MAX_SIZE).addOnSuccessListener {
            callback(true, it)
        }.addOnFailureListener {
            Log.d("excD", it.toString())
            callback(false, null)
        }

    }

    companion object{
        // @Volatile - Writes to this property are immediately visible to other threads
        @Volatile private var instance: StorageDao? = null

        // The only way to get hold of the FakeDatabase object
        fun getInstance() =
        // Already instantiated? - return the instance
            // Otherwise instantiate in a thread-safe manner
            instance ?: synchronized(this) {
                // If it's still not instantiated, finally create an object
                // also set the "instance" property to be the currently created one
                instance ?: StorageDao().also { instance = it }
            }
    }
}