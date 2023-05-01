package com.fabrika.pampaza.profile.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.fabrika.pampaza.common.utils.BaseViewModel
import com.fabrika.pampaza.firebase.FirebaseRepositoryImpl
import com.fabrika.pampaza.firebase.FirebaseRepository
import com.fabrika.pampaza.common.model.UserEntity
import com.fabrika.pampaza.profile.model.ProfileObj
import com.fabrika.pampaza.utils.SharedPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(application: Application) : BaseViewModel(application){

//    private val repository = FirebaseRepository()
    private var repository: FirebaseRepository = FirebaseRepositoryImpl()
    var isLoading = MutableLiveData<Boolean>()
    var isError = MutableLiveData<Boolean>()
    var data = MutableLiveData<String>()
    var allPosts = MutableLiveData<List<ProfileObj.ProfilePostEntity>>()
    var isLikeError = MutableLiveData<Boolean>()
    var lastClickedItemIndex: Int? = null
    var userEntity = MutableLiveData<UserEntity>()

    fun getOwnPostsWithPagination(offset: Long, limit: Long){
        launch {
            withContext(Dispatchers.IO){
                repository.getOwnPostsWithPagination(offset, limit, SharedPref.read(SharedPref.USER_ID, "") ?: "")
                    .collect{ result ->
                        result.data.let {
                            allPosts.postValue(it.filterNotNull())
                        }
                    }
            }
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