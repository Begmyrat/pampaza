package com.fabrika.pampaza.common.model

data class UserModel(
    val fullname: String?,
    val userId: String?,
    val createdAt: Long?,
    val followersCount: Long?,
    val followingCount: Long?
)
