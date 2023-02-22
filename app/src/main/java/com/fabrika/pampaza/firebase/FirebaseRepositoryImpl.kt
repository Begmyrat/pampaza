package com.fabrika.pampaza.firebase

import com.fabrika.pampaza.common.model.UserModel
import com.fabrika.pampaza.common.utils.BaseResult
import com.fabrika.pampaza.home.model.PostEntity
import com.fabrika.pampaza.login.model.UserEntity
import kotlinx.coroutines.flow.Flow

interface FirebaseRepositoryImpl {
    fun getAllPosts(): Flow<BaseResult.Success<List<PostEntity?>>>
    fun post(
        body: String,
        imageUrl: String?,
        originalPostId: String?
    ): Flow<BaseResult.Success<Boolean>>
    fun getUser(username: String, password: String): Flow<BaseResult.Success<UserEntity>>

}