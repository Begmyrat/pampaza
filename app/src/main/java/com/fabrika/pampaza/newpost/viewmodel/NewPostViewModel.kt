package com.fabrika.pampaza.newpost.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.fabrika.pampaza.common.utils.BaseViewModel
import com.fabrika.pampaza.firebase.FirebaseRepositoryImpl
import com.fabrika.pampaza.newpost.model.PublicityType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewPostViewModel(application: Application) : BaseViewModel(application) {
    private val repository = FirebaseRepositoryImpl()
    var isError = MutableLiveData<Boolean>()
    var isLoading = MutableLiveData<Boolean>()
    var data = MutableLiveData<String>()
    var publicity = MutableLiveData("PUBLIC")
    var isValidationError = MutableLiveData<Boolean>()

    fun post(
        body: String,
        imageUrl: String?,
        originalPostId: String?,
        originalPostAuthorName: String?,
        originalPostBody: String?,
        originalPostImageUrl: String?,
        originalPostAuthorId: String?,
        originalPostAuthorAvatarUrl: String?,
        originalPostDate: Long?,
        originalPostRepostCount: Long?,
        originalPostLikeCount: Long?
    ) {
        if (body.length < 10) {
            isValidationError.postValue(true)
            return
        }
        isLoading.postValue(true)
        launch {
            withContext(Dispatchers.IO) {
                publicity.value?.let {
                    repository.post(
                        body,
                        it,
                        imageUrl,
                        originalPostId,
                        originalPostAuthorName,
                        originalPostBody,
                        originalPostImageUrl,
                        originalPostAuthorId,
                        originalPostAuthorAvatarUrl,
                        originalPostDate,
                        originalPostRepostCount,
                        originalPostLikeCount
                    )
                        .collect { result ->
                            result.data.let { status ->
                                isError.postValue(status)
                            }
                            isLoading.postValue(false)
                        }
                }
            }
        }
    }

    fun setPublicity(type: PublicityType) {
        publicity.value = type.name
    }
}