package com.fabrika.pampaza.home.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.fabrika.pampaza.MainActivity
import com.fabrika.pampaza.common.utils.BaseViewModel
import com.fabrika.pampaza.firebase.FirebaseRepositoryImpl
import com.fabrika.pampaza.firebase.FirebaseRepository
import com.fabrika.pampaza.home.model.PostEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(application: Application) : BaseViewModel(application){

//    private val repository = FirebaseRepository()
    private var repository: FirebaseRepository = FirebaseRepositoryImpl()
    var isLoading = MutableLiveData<Boolean>()
    var isError = MutableLiveData<Boolean>()
    var data = MutableLiveData<String>()
    var allPosts = MutableLiveData<List<PostEntity>>()
    var isLikeError = MutableLiveData<Boolean>()

    fun getAllPosts(){
        launch {
            withContext(Dispatchers.IO){
                repository.getAllPosts()
                    .collect{ result ->
                        result.data.let {
                            allPosts.postValue(it.filterNotNull())
                        }
                    }
            }
        }
    }

    fun getPostsWithPagination(offset: Long, limit: Long){
        launch {
            withContext(Dispatchers.IO){
                repository.getPostsWithPagination(offset, limit)
                    .collect{ result ->
                        result.data.let {
                            allPosts.postValue(it.filterNotNull())
                        }
                    }
            }
        }
    }

    fun likePost(activity: MainActivity, postId: String){
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
}