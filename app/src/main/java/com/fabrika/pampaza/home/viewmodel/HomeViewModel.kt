package com.fabrika.pampaza.home.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fabrika.pampaza.common.utils.BaseViewModel
import com.fabrika.pampaza.firebase.FirebaseRepository
import com.fabrika.pampaza.firebase.FirebaseRepositoryImpl
import com.fabrika.pampaza.home.model.PostEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(application: Application) : BaseViewModel(application){

//    private val repository = FirebaseRepository()
    private var repository: FirebaseRepositoryImpl = FirebaseRepository()
    var isLoading = MutableLiveData<Boolean>()
    var isError = MutableLiveData<Boolean>()
    var data = MutableLiveData<String>()
    var allPosts = MutableLiveData<List<PostEntity>>()

    fun getAllPosts(){
        launch {
            withContext(Dispatchers.IO){
                repository.getAllPosts()
                    .collect{ result ->
                        result.data.let {
                            allPosts.postValue(it.filterNotNull())
                        }
                    }
                    .runCatching {
                        Log.d("ERR:", "hata verdi")
                    }
            }
        }
    }

}