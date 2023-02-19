package com.fabrika.pampaza.newpost.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.fabrika.pampaza.common.utils.BaseViewModel
import com.fabrika.pampaza.firebase.FirebaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewPostViewModel(application: Application) : BaseViewModel(application){
    private val repository = FirebaseRepository()
    var isLoading = MutableLiveData<Boolean>()
    var isError = MutableLiveData<Boolean>()
    var data = MutableLiveData<String>()

    fun post(
        body: String,
        imageUrl: String?,
        originalPostId: String?
    ){
        launch {
            withContext(Dispatchers.IO){
                repository.post(body, imageUrl, originalPostId)
                    .collect{ result ->
                        result.data.let {
                            isError.postValue(it)
                        }
                    }
            }
        }
    }
}