package com.fabrika.pampaza.profile.model

sealed class ProfileObj {
    data class ProfileUserEntity(
        var id: String? = null,
        var createdAt: Long? = null,
        var followersCount: Long? = null,
        var followingCount: Long? = null,
        var username: String? = null,
        var imageUrl: String? = null,
        var likedPosts: MutableList<String>? = null,
        var savedPosts: MutableList<Long>? = null,
        var userId: String? = null,
        var password: String? = null,
        var status: String? = null
    ): ProfileObj()

    data class ProfilePostEntity(
        var id: String? = null,
        val authorAvatarUrl: String? = null,
        val authorId: String? = null,
        val authorName: String? = null,
        val body: String? = null,
        var commentCount : Long? = null,
        val complaintCount : Long? = null,
        val date : Long? = null,
        val imageUrl : String? = null,
        val likeCount : Long? = null,
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
    ): ProfileObj()
}
