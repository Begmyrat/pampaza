package com.fabrika.pampaza.newpost.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.fabrika.pampaza.common.utils.BaseViewModel
import com.fabrika.pampaza.firebase.FirebaseRepositoryImpl
import com.fabrika.pampaza.newpost.model.PublicityType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewPostViewModel(application: Application) : BaseViewModel(application){
    private val repository = FirebaseRepositoryImpl()
    var isLoading = MutableLiveData<Boolean>()
    var isError = MutableLiveData<Boolean>()
    var data = MutableLiveData<String>()
    var publicity: String = "public"

    fun post(
        body: String,
        imageUrl: String?,
        originalPostId: String?
    ){
        launch {
            withContext(Dispatchers.IO){
                repository.post(body, publicity, imageUrl, originalPostId)
                    .collect{ result ->
                        result.data.let {
                            isError.postValue(it)
                        }
                    }
            }
        }
    }

    fun setPublicity(type: PublicityType){
        publicity = when(type){
            PublicityType.PUBLIC -> {
                "public"
            }
            PublicityType.FRIENDS -> {
                "friends"
            }
            PublicityType.OWN -> {
                "own"
            }
        }
    }
}