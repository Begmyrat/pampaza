package com.fabrika.pampaza.postDetail.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.fabrika.pampaza.MainActivity
import com.fabrika.pampaza.common.utils.BaseViewModel
import com.fabrika.pampaza.firebase.FirebaseRepository
import com.fabrika.pampaza.firebase.FirebaseRepositoryImpl
import com.fabrika.pampaza.home.model.PostEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PostDetailViewModel(application: Application) : BaseViewModel(application){
    private var repository: FirebaseRepository = FirebaseRepositoryImpl()
    var isLoading = MutableLiveData<Boolean>()
    var data = MutableLiveData<String>()
    var allComments = MutableLiveData<List<PostEntity>>()
    var isLikeError = MutableLiveData<Boolean>()
    var isPostCommentError = MutableLiveData<Boolean>()

    fun getComments(postId: String){
        launch {
            withContext(Dispatchers.IO){
                repository.getComments(postId)
                    .collect{ result ->
                        result.data.let {
                            allComments.postValue(it.filterNotNull())
                        }
                    }
            }
        }
    }

    fun postComment(postId: String, comment: String,){
        if(comment.isEmpty()){
            return
        }
        launch {
            withContext(Dispatchers.IO){
                repository.postComment(postId, comment)
                    .collect{ result ->
                        result.data.let {
                            isPostCommentError.postValue(it)
                        }
                    }
            }
        }
    }

    fun likePost(activity: MainActivity, entity: PostEntity){
        launch {
            withContext(Dispatchers.IO){
                entity.id?.let {
                    repository.likePost(activity, it)
                        .collect{ result ->
                            result.data.let { status ->
                                isLikeError.postValue(status)
                            }
                        }
                }
            }
        }
    }
}