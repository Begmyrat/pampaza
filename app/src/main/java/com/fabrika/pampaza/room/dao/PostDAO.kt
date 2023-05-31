package com.fabrika.pampaza.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.fabrika.pampaza.room.model.PostModel

@Dao
interface PostDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(postModel: PostModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(values: List<PostModel>)

    @Update
    suspend fun updatePost(postModel: PostModel)

    @Query("SELECT * FROM pampaza_posts_table")
    fun getAllPosts(): LiveData<List<PostModel>>

    @Query("DELETE FROM pampaza_posts_table")
    fun deleteAllPosts()

    @Delete
    suspend fun deletePost(postModel: PostModel)

}