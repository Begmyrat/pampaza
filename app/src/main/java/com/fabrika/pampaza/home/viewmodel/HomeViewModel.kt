package com.fabrika.pampaza.home.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.fabrika.pampaza.MainActivity
import com.fabrika.pampaza.common.utils.BaseResult
import com.fabrika.pampaza.common.utils.BaseViewModel
import com.fabrika.pampaza.firebase.FirebaseRepositoryImpl
import com.fabrika.pampaza.firebase.FirebaseRepository
import com.fabrika.pampaza.home.model.PostEntity
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(application: Application) : BaseViewModel(application) {

    //    private val repository = FirebaseRepository()
    private var repository: FirebaseRepository = FirebaseRepositoryImpl()
    var isLoading = MutableLiveData<Boolean>()
    var isError = MutableLiveData<Boolean>()
    var data = MutableLiveData<String>()
    var allPosts = MutableLiveData<List<PostEntity>>()
    var isLikeError = MutableLiveData<Boolean>()
    var lastClickedItemIndex: Int? = null
    var OFFSET = Long.MAX_VALUE
    var OFFSET_OLD = -1L

    fun getAllPosts() {
        launch {
            withContext(Dispatchers.IO) {
                repository.getAllPosts()
                    .collect { result ->
                        result.data.let {
                            allPosts.postValue(it.filterNotNull())
                        }
                    }
            }
        }
    }

    private val db = Firebase.firestore
    fun getPostsWithPagination(offset: Long, limit: Long) {
        isLoading.postValue(true)
        OFFSET = offset
        launch {
            withContext(Dispatchers.IO) {
//                repository.getPostsWithPagination(offset, limit)
//                    .collect{ result ->
//                        result.data.let {
//                            allPosts.postValue(it.filterNotNull())
//                        }
//                    }

                db.collection("Posts")
                    .document("Data")
                    .collection("List")
                    .orderBy("date", Query.Direction.DESCENDING)
                    .limit(limit)
                    .startAfter(offset)
                    .get()
                    .addOnSuccessListener {
                        if (it.documents.isNotEmpty()) {
                            val list = it.documents.map { docSnapshot ->
                                docSnapshot.toObject(PostEntity::class.java)
                            }
                            it.documents.mapIndexed { index, documentSnapshot ->
                                list[index]?.id = documentSnapshot.id
                            }
                            allPosts.postValue(list.filterNotNull())
                            isLoading.postValue(false)
                        }
                    }
                    .addOnFailureListener {
                        isLoading.postValue(false)
                    }
                    .addOnCanceledListener {
                        isLoading.postValue(false)
                    }
                    .addOnCompleteListener {
                        isLoading.postValue(false)
                    }
            }
        }
    }

    fun likePost(activity: MainActivity, postId: String) {
        launch {
            withContext(Dispatchers.IO) {
                repository.likePost(postId)
                    .collect { result ->
                        result.data.let { status ->
                            isLikeError.postValue(status)
                        }
                    }
            }
        }
    }
}