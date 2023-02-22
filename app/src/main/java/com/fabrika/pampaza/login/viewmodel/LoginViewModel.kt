package com.fabrika.pampaza.login.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.fabrika.pampaza.common.utils.BaseViewModel
import com.fabrika.pampaza.firebase.FirebaseRepository
import com.fabrika.pampaza.firebase.FirebaseRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(application: Application) : BaseViewModel(application){

    private var repository: FirebaseRepositoryImpl = FirebaseRepository()
    var isLoading = MutableLiveData<Boolean>()
    var isError = MutableLiveData<Boolean>()
    var data = MutableLiveData<String>()

    fun getUser(userId: String, password: String){
        launch {
            withContext(Dispatchers.IO){
                repository.getUser(userId, password)
                    .collect{ result ->
                        result.data.let {
                            isError.postValue(it.password != password)
                        }
                    }
            }
        }
    }

}