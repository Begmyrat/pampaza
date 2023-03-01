package com.fabrika.pampaza.home.model

data class PostEntity(
    var id: String? = null,
    val authorAvatarUrl: String? = null,
    val authorId: String? = null,
    val authorName: String? = null,
    val body: String? = null,
    val commentCount : Long? = null,
    val complaintCount : Long? = null,
    val date : Long? = null,
    val imageUrl : String? = null,
    val likeCount : Long? = null,
    val originalPostId : String? = null,
    val rePostCount : Long? = null,
    val publicity: String? = null
)
