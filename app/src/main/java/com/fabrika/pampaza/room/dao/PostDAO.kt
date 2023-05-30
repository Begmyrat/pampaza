package com.fabrika.pampaza.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.fabrika.pampaza.room.model.PostModel

@Dao
interface PostDAO {

    @Insert
    suspend fun insertPost(postModel: PostModel)

    @Update
    suspend fun updatePost(postModel: PostModel)

    @Query("SELECT * FROM pampaza_post_table")
    fun getAllPosts(): LiveData<List<PostModel>>

    @Delete
    suspend fun deletePost(postModel: PostModel)

}