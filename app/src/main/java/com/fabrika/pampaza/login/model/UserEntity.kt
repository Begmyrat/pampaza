package com.fabrika.pampaza.login.model

data class UserEntity(
    var id: String? = null,
    var createdAt: Long? = null,
    var followersCount: Long? = null,
    var followingCount: Long? = null,
    var fullname: String? = null,
    var imageUrl: String? = null,
    var likedPosts: MutableList<String>? = null,
    var savedPosts: MutableList<Long>? = null,
    var userId: String? = null,
    var password: String? = null
)
