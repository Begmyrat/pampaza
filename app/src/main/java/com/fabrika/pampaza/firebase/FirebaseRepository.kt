package com.fabrika.pampaza.firebase

import com.fabrika.pampaza.common.utils.BaseResult
import com.fabrika.pampaza.home.model.PostEntity
import com.fabrika.pampaza.common.model.UserEntity
import com.fabrika.pampaza.profile.model.ProfileObj
import kotlinx.coroutines.flow.Flow
import java.io.File

interface FirebaseRepository {
    fun getAllPosts(): Flow<BaseResult.Success<List<PostEntity?>>>

    fun getPostsWithPagination(offset: Long, limit: Long): Flow<BaseResult.Success<List<PostEntity?>>>

    fun getOwnPostsWithPagination(offset: Long, limit: Long, userId: String): Flow<BaseResult.Success<List<ProfileObj.ProfilePostEntity?>>>

    fun getComments(postId: String): Flow<BaseResult.Success<List<PostEntity?>>>

    fun postComment(postId: String, comment: String, currentCommentCoung: Long): Flow<BaseResult.Success<Boolean>>

    fun putPersonalInformation(username: String, bio: String, address: String, birthday: Long?, avatar: File?, background: File?, avatarUrl: String, backgroundUrl: String): Flow<BaseResult.Success<Boolean>>

    fun post(
        body: String,
        publicity: String,
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
    ): Flow<BaseResult.Success<Boolean>>

    fun deletePost(entity: ProfileObj.ProfilePostEntity): Flow<BaseResult.Success<Boolean>>

    fun getUser(username: String, password: String): Flow<BaseResult.Success<UserEntity>>

    fun signUp(username: String, password: String): Flow<BaseResult.Success<UserEntity>>

    fun likePost(postId: String): Flow<BaseResult.Success<Boolean>>

    fun search(text: String): Flow<BaseResult.Success<List<PostEntity?>>>
}