package com.fabrika.pampaza.login.model

data class UserEntity(
    val id: Long? = null,
    val createdAt: Long? = null,
    val followersCount: Long? = null,
    val followingCount: Long? = null,
    val fullname: String? = null,
    val imageUrl: String? = null,
    val likedPosts: List<Long>? = null,
    val savedPosts: List<Long>? = null,
    val userId: String? = null,
    val password: String? = null
)
