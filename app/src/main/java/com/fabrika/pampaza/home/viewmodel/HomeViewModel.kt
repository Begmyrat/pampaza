package com.fabrika.pampaza.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel: ViewModel(){

//    private val repository = FirebaseRepository()
    var isLoading = MutableLiveData<Boolean>()
    var isError = MutableLiveData<Boolean>()
    var data = MutableLiveData<String>()

    fun getUserData(){

    }

}