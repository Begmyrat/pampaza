package com.fabrika.pampaza.home.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fabrika.pampaza.common.utils.BaseViewModel

class HomeViewModel(application: Application) : BaseViewModel(application){

//    private val repository = FirebaseRepository()
    var isLoading = MutableLiveData<Boolean>()
    var isError = MutableLiveData<Boolean>()
    var data = MutableLiveData<String>()

    fun getUserData(){

    }

}