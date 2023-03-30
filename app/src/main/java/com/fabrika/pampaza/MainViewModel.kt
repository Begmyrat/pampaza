package com.fabrika.pampaza

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fabrika.pampaza.common.utils.BaseViewModel
import com.fabrika.pampaza.firebase.FirebaseRepository
import com.fabrika.pampaza.firebase.FirebaseRepositoryImpl
import com.fabrika.pampaza.home.model.PostEntity
import com.fabrika.pampaza.login.model.LoginStatusType
import com.fabrika.pampaza.login.model.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : BaseViewModel(application) {

    private var repository: FirebaseRepository = FirebaseRepositoryImpl()
    var isLoading = MutableLiveData<Boolean>()
    var isError = MutableLiveData<Boolean>()
    var userEntity = MutableLiveData<UserEntity>()
    var likePostIds = MutableLiveData<MutableList<String>>()
    var isSplash = false
    var isLikeError = MutableLiveData<Boolean>()

    fun likePost(postId: String){
        launch {
            withContext(Dispatchers.IO){
                repository.likePost(postId)
                    .collect{ result ->
                        result.data.let { status ->
                            isLikeError.postValue(status)
                        }
                    }
            }
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