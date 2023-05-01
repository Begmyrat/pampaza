package com.fabrika.pampaza.login.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.fabrika.pampaza.common.utils.BaseViewModel
import com.fabrika.pampaza.firebase.FirebaseRepositoryImpl
import com.fabrika.pampaza.firebase.FirebaseRepository
import com.fabrika.pampaza.common.model.LoginStatusType
import com.fabrika.pampaza.utils.SharedPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(application: Application) : BaseViewModel(application){

    private var repository: FirebaseRepository = FirebaseRepositoryImpl()
    var isLoading = MutableLiveData<Boolean>()
    var status = MutableLiveData<LoginStatusType>()
    var data = MutableLiveData<String>()

    fun getUser(userId: String, password: String){
        launch {
            withContext(Dispatchers.IO){
                repository.getUser(userId, password)
                    .collect{ result ->
                        result.data.let {
                            status.postValue(if(it.password != password) LoginStatusType.FAIL else LoginStatusType.SUCCESS )
                        }
                    }
            }
        }
    }

    fun signUp(username: String, password: String, passwordConfirmation: String){
        if(username.isEmpty() || password.isEmpty() || passwordConfirmation.isEmpty()){
            status.postValue(LoginStatusType.ERROR_FILL_THE_BLANKS)
            return
        } else if(password != passwordConfirmation){
            status.postValue(LoginStatusType.ERROR_PASSWORD_CONFIRMATION)
            return
        }

        launch {
            withContext(Dispatchers.IO){
                repository.signUp(username, password)
                    .collect{ result ->
                        result.data.let {
                            it.userId?.let {username ->
                                saveData(username)
                            }
                            status.postValue(if(it.id != null) LoginStatusType.SUCCESS else LoginStatusType.FAIL)
                        }
                    }
            }
        }
    }

    private fun saveData(username: String) {
        SharedPref.write(SharedPref.IS_LOGGED_IN, true)
        SharedPref.write(SharedPref.USERNAME, username)
    }

    fun splashDelay(milliseconds: Long){
        launch {
            delay(milliseconds)
            status.postValue(LoginStatusType.SPLASH_SUCCESS)
        }
    }
}