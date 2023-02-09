package com.fabrika.pampaza.home.model

data class PostEntity(
    val id: String?,
    val authorAvatarUrl: String?,
    val authorId: String?,
    val authorName: String?,
    val commentCount : Long?,
    val complaintCount : Long?,
    val date : Long?,
    val imageUrl : String?,
    val likeCount : Long?,
    val originalPostId : String?,
    val rePostCount : Long?
)
