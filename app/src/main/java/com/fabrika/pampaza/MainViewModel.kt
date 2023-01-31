package com.fabrika.pampaza

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    var isLoading = MutableLiveData<Boolean>()
    var isError = MutableLiveData<Boolean>()
    var data = MutableLiveData<String>()

}