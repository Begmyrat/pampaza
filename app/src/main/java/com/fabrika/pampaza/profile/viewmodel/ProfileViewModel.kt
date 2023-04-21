package com.fabrika.pampaza.profile.viewmodel

import android.app.Application
import android.content.SharedPreferences
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
}