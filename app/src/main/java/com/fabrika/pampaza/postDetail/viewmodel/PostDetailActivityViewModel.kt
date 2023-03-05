package com.fabrika.pampaza.postDetail.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.fabrika.pampaza.common.utils.BaseViewModel
import com.fabrika.pampaza.firebase.FirebaseRepository
import com.fabrika.pampaza.firebase.FirebaseRepositoryImpl
import com.fabrika.pampaza.login.model.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PostDetailActivityViewModel(application: Application) : BaseViewModel(application) {

    private var repository: FirebaseRepository = FirebaseRepositoryImpl()
    var userEntity = MutableLiveData<UserEntity>()

    fun likePost(id: String){
        val temp = userEntity.value
        temp?.likedPosts?.add(id)
        temp.let {
            userEntity.postValue(it)
        }
    }

    fun dislikePost(id: String){
        val temp = userEntity.value
        temp?.likedPosts?.remove(id)
        temp.let {
            userEntity.postValue(it)
        }
    }

    fun getUser(userId: String, password: String){
        launch {
            withContext(Dispatchers.IO){
                repository.getUser(userId, password)
                    .collect{ result ->
                        result.data.let {
                            userEntity.postValue(it)
                        }
                    }
            }
        }
    }

}