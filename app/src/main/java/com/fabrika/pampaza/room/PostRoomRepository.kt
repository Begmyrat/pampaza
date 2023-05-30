package com.fabrika.pampaza.room

import com.fabrika.pampaza.room.dao.PostDAO
import com.fabrika.pampaza.room.model.PostModel

class PostRoomRepository (private val postDAO: PostDAO) {
    suspend fun insertPost(postModel: PostModel) = postDAO.insertPost(postModel)
    suspend fun updatePost(postModel: PostModel) = postDAO.updatePost(postModel)
    suspend fun deletePost(postModel: PostModel) = postDAO.deletePost(postModel)
    fun getAllPosts() = postDAO.getAllPosts()
}