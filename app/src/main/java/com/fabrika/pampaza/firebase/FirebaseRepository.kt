package com.fabrika.pampaza.firebase

import com.fabrika.pampaza.MainActivity
import com.fabrika.pampaza.common.utils.BaseResult
import com.fabrika.pampaza.home.model.PostEntity
import com.fabrika.pampaza.login.model.UserEntity
import kotlinx.coroutines.flow.Flow

interface FirebaseRepository {
    fun getAllPosts(): Flow<BaseResult.Success<List<PostEntity?>>>

    fun post(
        body: String,
        publicity: String,
        imageUrl: String?,
        originalPostId: String?
    ): Flow<BaseResult.Success<Boolean>>

    fun getUser(username: String, password: String): Flow<BaseResult.Success<UserEntity>>

    fun signUp(username: String, password: String): Flow<BaseResult.Success<UserEntity>>

    fun likePost(activity: MainActivity, postId: String): Flow<BaseResult.Success<Boolean>>

}