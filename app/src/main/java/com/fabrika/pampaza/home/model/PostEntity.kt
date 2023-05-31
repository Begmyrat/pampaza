package com.fabrika.pampaza.home.model

import com.fabrika.pampaza.room.model.PostModel

data class PostEntity(
    var id: String? = null,
    val authorAvatarUrl: String? = null,
    val authorId: String? = null,
    val authorName: String? = null,
    val body: String? = null,
    var commentCount : Long? = null,
    val complaintCount : Long? = null,
    val date : Long? = null,
    val imageUrl : String? = null,
    var likeCount : Long? = null,
    val rePostCount : Long? = null,
    val publicity: String? = null,
    val originalPostId : String? = null,
    val originalPostBody: String? = null,
    val originalPostRepostCount: Long? = null,
    val originalPostLikeCount: Long? = null,
    val originalPostDate: Long? = null,
    val originalPostImageUrl: String? = null,
    val originalPostAuthorAvatarUrl: String? = null,
    val originalPostAuthorId: String? = null,
    val originalPostAuthorName: String? = null
) {
    fun toPostModel() = PostModel(
        id = null,
        postId = id,
        authorAvatarUrl = authorAvatarUrl,
        authorId = authorId,
        authorName = authorName,
        body = body,
        commentCount = commentCount,
        complaintCount = complaintCount,
        date = date,
        imageUrl = imageUrl,
        likeCount = likeCount,
        rePostCount = rePostCount,
        publicity = publicity,
        originalPostId = originalPostId,
        originalPostBody = originalPostBody,
        originalPostRepostCount = originalPostRepostCount,
        originalPostLikeCount = originalPostLikeCount,
        originalPostDate = originalPostDate,
        originalPostImageUrl = originalPostImageUrl,
        originalPostAuthorAvatarUrl = originalPostAuthorAvatarUrl,
        originalPostAuthorId = originalPostAuthorId,
        originalPostAuthorName = originalPostAuthorName
    )
}
