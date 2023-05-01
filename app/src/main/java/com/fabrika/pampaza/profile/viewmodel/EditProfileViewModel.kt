package com.fabrika.pampaza.profile.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.fabrika.pampaza.MainActivity
import com.fabrika.pampaza.common.utils.BaseViewModel
import com.fabrika.pampaza.firebase.FirebaseRepositoryImpl
import com.fabrika.pampaza.firebase.FirebaseRepository
import com.fabrika.pampaza.home.model.PostEntity
import com.fabrika.pampaza.profile.model.ProfileObj
import com.fabrika.pampaza.utils.SharedPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

class EditProfileViewModel(application: Application) : BaseViewModel(application){

    private var repository: FirebaseRepository = FirebaseRepositoryImpl()
    var isLoading = MutableLiveData<Boolean>()
    var isError = MutableLiveData<Boolean>()
    var backgroundImage: MutableLiveData<File> = MutableLiveData()
    var avatarImage: MutableLiveData<File> = MutableLiveData()
    var birthday: Long? = null
    var avatarImageUrl: String = ""
    var backgroundImageUrl: String = ""

    fun addBackgroundImage(file: File){
        backgroundImage.postValue(file)
    }

    fun addAvatarImage(file: File){
        avatarImage.postValue(file)
    }

    fun saveData(username: String, bio: String, address: String){
        isLoading.postValue(true)
        launch {
            withContext(Dispatchers.IO){
                repository.putPersonalInformation(username, bio, address, birthday, avatarImage.value, backgroundImage.value, avatarImageUrl, backgroundImageUrl)
                    .catch {
                        Log.d("UpdateProfileError: ", it.message.toString())
                    }
                    .collect{ result ->
                        result.data.let {
                            isError.postValue(!it)
                            isLoading.postValue(false)
                        }
                    }
            }
        }
    }

    fun saveBitmapToFile(context: Context, file: File, isAvatar: Boolean): File? {
        return try {

            // BitmapFactory options to downsize the image
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            o.inSampleSize = 1
            // factor of downsizing the image
            var inputStream = FileInputStream(file)
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o)
            inputStream.close()

            // The new size we want to scale to
            val REQUIRED_SIZE = 256

            // Find the correct scale value. It should be the power of 2.
            var scale = 1
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                o.outHeight / scale / 2 >= REQUIRED_SIZE
            ) {
                scale *= 2
            }
            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            inputStream = FileInputStream(file)
            val selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2)
            inputStream.close()

            val newFile = File(context.filesDir, "${Calendar.getInstance().timeInMillis}.png")

            val outputStream = FileOutputStream(newFile)
            selectedBitmap?.compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
            newFile
        } catch (e: java.lang.Exception) {
            null
        }
    }
}