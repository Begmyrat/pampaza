package com.fabrika.pampaza.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pampaza_posts_table")
data class PostModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "postId")
    val postId: String?,
    @ColumnInfo(name = "authorAvatarUrl")
    val authorAvatarUrl: String?,
    @ColumnInfo(name = "authorId")
    val authorId: String?,
    @ColumnInfo(name = "authorName")
    val authorName: String?,
    @ColumnInfo(name = "body")
    val body: String?,
    @ColumnInfo(name = "commentCount")
    var commentCount : Long?,
    @ColumnInfo(name = "complaintCount")
    val complaintCount : Long?,
    @ColumnInfo(name = "date")
    val date : Long?,
    @ColumnInfo(name = "imageUrl")
    val imageUrl : String?,
    @ColumnInfo(name = "likeCount")
    var likeCount : Long?,
    @ColumnInfo(name = "rePostCount")
    val rePostCount : Long?,
    @ColumnInfo(name = "publicity")
    val publicity: String?,
    @ColumnInfo(name = "originalPostId")
    val originalPostId : String?,
    @ColumnInfo(name = "originalPostBody")
    val originalPostBody: String?,
    @ColumnInfo(name = "originalPostRepostCount")
    val originalPostRepostCount: Long?,
    @ColumnInfo(name = "originalPostLikeCount")
    val originalPostLikeCount: Long?,
    @ColumnInfo(name = "originalPostDate")
    val originalPostDate: Long?,
    @ColumnInfo(name = "originalPostImageUrl")
    val originalPostImageUrl: String?,
    @ColumnInfo(name = "originalPostAuthorAvatarUrl")
    val originalPostAuthorAvatarUrl: String?,
    @ColumnInfo(name = "originalPostAuthorId")
    val originalPostAuthorId: String?,
    @ColumnInfo(name = "originalPostAuthorName")
    val originalPostAuthorName: String?
)
