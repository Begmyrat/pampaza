package com.fabrika.pampaza.firebase

import androidx.core.net.toUri
import com.fabrika.pampaza.MainActivity
import com.fabrika.pampaza.common.utils.BaseResult
import com.fabrika.pampaza.home.model.PostEntity
import com.fabrika.pampaza.common.model.UserEntity
import com.fabrika.pampaza.profile.model.ProfileObj
import com.fabrika.pampaza.utils.SharedPref
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.io.File
import java.util.*
import kotlin.collections.HashMap

class FirebaseRepositoryImpl : FirebaseRepository {

    private val db = Firebase.firestore
    override fun getAllPosts(): Flow<BaseResult.Success<List<PostEntity?>>> = callbackFlow {
        val ref = db.collection("Posts")
            .document("Data")
            .collection("List")
            .orderBy("id", Query.Direction.DESCENDING)

        val listener = ref.addSnapshotListener { value, _ ->
            if (value?.documents?.isNotEmpty() == true) {
                val list = value.documents.map { docSnapshot ->
                    docSnapshot.toObject(PostEntity::class.java)
                }

                value.documents.mapIndexed { index, documentSnapshot ->
                    list[index]?.id = documentSnapshot.id
                }

                BaseResult.Success(list).let {
                    trySend(it).isSuccess
                }
            }
        }
        awaitClose { listener.remove() }
    }

    override fun getPostsWithPagination(
        offset: Long,
        limit: Long
    ): Flow<BaseResult.Success<List<PostEntity?>>> = callbackFlow {
        val ref = db.collection("Posts")
            .document("Data")
            .collection("List")
            .orderBy("date", Query.Direction.DESCENDING)
            .limit(limit)
            .startAfter(offset)

        val listener = ref.addSnapshotListener { value, _ ->
            if (value?.documents?.isNotEmpty() == true) {
                val list = value.documents.map { docSnapshot ->
                    docSnapshot.toObject(PostEntity::class.java)
                }

                value.documents.mapIndexed { index, documentSnapshot ->
                    list[index]?.id = documentSnapshot.id
                }

                BaseResult.Success(list).let {
                    trySend(it).isSuccess
                }
            }
        }
        awaitClose { listener.remove() }
    }

    override fun getOwnPostsWithPagination(
        offset: Long,
        limit: Long,
        userId: String
    ): Flow<BaseResult.Success<List<ProfileObj.ProfilePostEntity?>>> = callbackFlow {
        val ref = db.collection("Posts")
            .document("Data")
            .collection("List")
            .orderBy("date", Query.Direction.DESCENDING)
            .whereEqualTo("authorId", userId)
            .limit(limit)
            .startAfter(offset)

        val listener = ref.addSnapshotListener { value, _ ->
            if (value?.documents?.isNotEmpty() == true) {
                val list = value.documents.map { docSnapshot ->
                    docSnapshot.toObject(ProfileObj.ProfilePostEntity::class.java)
                }

                value.documents.mapIndexed { index, documentSnapshot ->
                    list[index]?.id = documentSnapshot.id
                }

                BaseResult.Success(list).let {
                    trySend(it).isSuccess
                }
            }
        }
        awaitClose { listener.remove() }
    }

    override fun getComments(postId: String): Flow<BaseResult.Success<List<PostEntity?>>> =
        callbackFlow {
            val ref = db.collection("Comments")
                .document("Data")
                .collection("List")
                .whereEqualTo("postId", postId)
                .orderBy("date", Query.Direction.DESCENDING)

            val listener = ref.addSnapshotListener { value, error ->
                if (value?.documents?.isNotEmpty() == true) {
                    val list = value.documents.map { docSnapshot ->
                        docSnapshot.toObject(PostEntity::class.java)
                    }

                    value.documents.mapIndexed { index, documentSnapshot ->
                        list[index]?.id = documentSnapshot.id
                    }

                    BaseResult.Success(list).let {
                        trySend(it).isSuccess
                    }
                }
            }
            awaitClose { listener.remove() }
        }

    override fun postComment(
        postId: String,
        comment: String,
        currentCommentCount: Long
    ): Flow<BaseResult.Success<Boolean>> = callbackFlow {
        val milliseconds = Calendar.getInstance().timeInMillis
        val ref = db.collection("Comments")
            .document("Data")
            .collection("List")
            .add(
                hashMapOf(
                    "authorDeviceId" to "authorAvatarUrlValue",
                    "authorId" to "authorIdValue",
                    "authorName" to "Begmyrat",
                    "body" to comment,
                    "complaintCount" to 0,
                    "date" to milliseconds,
                    "mentionedUserDeviceId" to "mentionedUserDeviceId",
                    "mentionedUserId" to "userId",
                    "postAuthorDeviceId" to "postAuthorDeviceId",
                    "postAuthorId" to "authorId",
                    "postAuthorName" to "authorName",
                    "postId" to postId
                )
            )

        val listener = ref.addOnSuccessListener {
            db.collection("Posts")
                .document("Data")
                .collection("List")
                .document(postId)
                .update("commentCount", FieldValue.increment(currentCommentCount + 1))
                .addOnSuccessListener {
                    trySend(BaseResult.Success(true)).isSuccess
                }
                .addOnFailureListener {
                    trySend(BaseResult.Success(false)).isSuccess
                }
        }.addOnFailureListener {
            trySend(BaseResult.Success(false)).isSuccess
        }

        awaitClose { listener }
    }

    override fun putPersonalInformation(
        username: String,
        bio: String,
        address: String,
        birthday: Long?,
        avatar: File?,
        background: File?,
        avatarUrl: String,
        backgroundUrl: String
    ): Flow<BaseResult.Success<Boolean>> = callbackFlow {

        val dataMap = HashMap<String, Any>()
        dataMap["username"] = username
        dataMap["status"] = bio
        dataMap["address"] = address
        dataMap["authorAvatarUrl"] = avatarUrl
        dataMap["authorBackgroundUrl"] = backgroundUrl
        birthday?.let {
            dataMap["birthday"] = it
        }

        var listener: Task<Void>? = null

        avatar?.let { file ->
            val fileName = SharedPref.read(SharedPref.USER_ID, "") + "avatar"
            val ref = Firebase.storage.getReference("profile/$fileName")
            ref.putFile(file.toUri())
            ref.downloadUrl.addOnSuccessListener {
                dataMap["authorAvatarUrl"] = it
                listener = db.collection("Users")
                    .document(SharedPref.read(SharedPref.DOC_ID, "") ?: "")
                    .update(dataMap)
                    .addOnSuccessListener {
                        trySend(BaseResult.Success(true)).isSuccess
                    }
                    .addOnFailureListener {
                        trySend(BaseResult.Success(false)).isSuccess
                    }
            }
        }
        background?.let { file ->
            val fileName = SharedPref.read(SharedPref.USER_ID, "") + "background"
            val ref = Firebase.storage.getReference("profile/$fileName")
            ref.putFile(file.toUri())
            ref.downloadUrl.addOnSuccessListener {
                dataMap["authorBackgroundUrl"] = it
                listener = db.collection("Users")
                    .document(SharedPref.read(SharedPref.DOC_ID, "") ?: "")
                    .update(dataMap)
                    .addOnSuccessListener {
                        trySend(BaseResult.Success(true)).isSuccess
                    }
                    .addOnFailureListener {
                        trySend(BaseResult.Success(false)).isSuccess
                    }
            }
        }

        if (background == null && avatar == null) {
            listener = db.collection("Users")
                .document(SharedPref.read(SharedPref.DOC_ID, "") ?: "")
                .update(dataMap)
                .addOnSuccessListener {
                    trySend(BaseResult.Success(true)).isSuccess
                }
                .addOnFailureListener {
                    trySend(BaseResult.Success(false)).isSuccess
                }
        }

        awaitClose { listener }
    }

    override fun post(
        body: String,
        publicity: String,
        imageUrl: String?,
        originalPostId: String?,
        originalPostAuthorName: String?,
        originalPostBody: String?,
        originalPostImageUrl: String?,
        originalPostAuthorId: String?,
        originalPostAuthorAvatarUrl: String?,
        originalPostDate: Long?,
        originalPostRepostCount: Long?,
        originalPostLikeCount: Long?,
    ): Flow<BaseResult.Success<Boolean>> = callbackFlow {
        val milliseconds = Calendar.getInstance().timeInMillis
        val ref = db.collection("Posts")
            .document("Data")
            .collection("List")
            .add(
                hashMapOf(
                    "authorAvatarUrl" to SharedPref.read(SharedPref.AVATAR_URL, ""),
                    "authorId" to SharedPref.read(SharedPref.USER_ID, ""),
                    "authorName" to SharedPref.read(SharedPref.USERNAME, ""),
                    "body" to body,
                    "publicity" to publicity,
                    "commentCount" to 0,
                    "complaintCount" to 0,
                    "date" to milliseconds,
                    "id" to "$milliseconds",
                    "imageUrl" to imageUrl,
                    "likeCount" to 0,
                    "rePostCount" to 0,
                    "originalPostId" to originalPostId,
                    "originalPostBody" to originalPostBody,
                    "originalPostImageUrl" to originalPostImageUrl,
                    "originalPostRepostCount" to originalPostRepostCount,
                    "originalPostLikeCount" to originalPostLikeCount,
                    "originalPostAuthorName" to originalPostAuthorName,
                    "originalPostAuthorId" to originalPostAuthorId,
                    "originalPostAuthorAvatarUrl" to originalPostAuthorAvatarUrl
                )
            )

        val listener = ref.addOnSuccessListener {
            if (originalPostId != null) {
                db.collection("Posts")
                    .document("Data")
                    .collection("List")
                    .document(originalPostId)
                    .update("rePostCount", FieldValue.increment((originalPostRepostCount ?: 0) + 1))
                    .addOnSuccessListener {
                        trySend(BaseResult.Success(true)).isSuccess
                    }
                    .addOnFailureListener {
                        trySend(BaseResult.Success(false)).isSuccess
                    }
            } else {
                trySend(BaseResult.Success(true)).isSuccess
            }

        }.addOnFailureListener {
            trySend(BaseResult.Success(false)).isSuccess
        }

        awaitClose { listener }
    }

    override fun deletePost(entity: ProfileObj.ProfilePostEntity): Flow<BaseResult.Success<Boolean>> =
        callbackFlow {
            entity.id?.let {
                val ref = db.collection("Posts")
                    .document("Data")
                    .collection("List")
                    .document(it)
                    .delete()

                val listener = ref.addOnSuccessListener {
                    trySend(BaseResult.Success(true)).isSuccess
                }.addOnFailureListener {
                    trySend(BaseResult.Success(false)).isSuccess
                }

                awaitClose { listener }
            }
        }


    override fun getUser(username: String, password: String): Flow<BaseResult.Success<UserEntity>> =
        callbackFlow {
            val ref = db.collection("Users")
                .whereEqualTo("userId", username)

            val listener = ref.addSnapshotListener { value, error ->
                if (value?.documents?.isNotEmpty() == true) {
                    val list = value.documents.map { docSnapshot ->
                        docSnapshot.toObject(UserEntity::class.java)
                    }

                    value.documents.mapIndexed { index, documentSnapshot ->
                        list[index]?.id = documentSnapshot.id
                    }

                    list.firstOrNull().let {
                        BaseResult.Success(it!!).let { result ->

                            SharedPref.write(SharedPref.USER_ID, result.data.userId)
                            SharedPref.write(SharedPref.USERNAME, result.data.username)
                            SharedPref.write(SharedPref.PASSWORD, result.data.password)
                            SharedPref.write(SharedPref.AVATAR_URL, result.data.authorAvatarUrl)
                            SharedPref.write(SharedPref.IS_LOGGED_IN, true)
                            SharedPref.write(SharedPref.DOC_ID, result.data.id)

                            trySend(result).isSuccess
                        }
                    }
                } else {
                    trySend(BaseResult.Success(UserEntity())).isSuccess
                }
            }
            awaitClose { listener.remove() }
        }

    override fun signUp(username: String, password: String): Flow<BaseResult.Success<UserEntity>> =
        callbackFlow {
            val milliseconds = Calendar.getInstance().timeInMillis
            val ref = db.collection("Users")
                .add(
                    hashMapOf(
                        "createdAt" to milliseconds,
                        "followersCount" to 0,
                        "followingCount" to 0,
                        "id" to "$milliseconds",
                        "password" to password,
                        "authorAvatarUrl" to "",
                        "username" to username,
                        "userId" to username,
                        "date" to milliseconds
                    )
                )

            val listener = ref.addOnSuccessListener {

                SharedPref.write(SharedPref.USER_ID, username)
                SharedPref.write(SharedPref.USERNAME, username)
                SharedPref.write(SharedPref.PASSWORD, password)
                SharedPref.write(SharedPref.AVATAR_URL, "")
                SharedPref.write(SharedPref.IS_LOGGED_IN, true)

                trySend(
                    BaseResult.Success(
                        UserEntity(
                            id = "$milliseconds",
                            createdAt = milliseconds,
                            followersCount = 0,
                            followingCount = 0,
                            username = username,
                            authorAvatarUrl = null,
                            likedPosts = null,
                            savedPosts = null,
                            userId = username,
                            password = password
                        )
                    )
                ).isSuccess
            }.addOnFailureListener {
                trySend(BaseResult.Success(UserEntity())).isSuccess
            }

            awaitClose { listener }
        }

    override fun likePost(
        postId: String
    ): Flow<BaseResult.Success<Boolean>> =
        callbackFlow {
            var listener: Task<Void>? = null

            MainActivity.viewmodel.userEntity.value?.let { user ->
                val ref = user.userId.let {
                    db
                        .collection("Users")
                        .document("${user.id}")
                }

                if (user.likedPosts == null) {
                    user.likedPosts = mutableListOf()
                }

                var likeCount = 0

                user.likedPosts.let {
                    if (it?.contains(postId) == true) {
                        it.remove(postId)
                        likeCount--
                    } else {
                        it?.add(postId)
                        likeCount++
                    }
                }

                listener = ref.update("likedPosts", user.likedPosts)
                    .addOnSuccessListener {
                        db
                            .collection("Posts")
                            .document("Data")
                            .collection("List")
                            .document("$postId")
                            .update("likeCount", FieldValue.increment(likeCount.toLong()))
                            .addOnSuccessListener {
                                trySend(BaseResult.Success(true)).isSuccess
                            }
                            .addOnFailureListener {
                                trySend(BaseResult.Success(false)).isSuccess
                            }
                    }
                    .addOnFailureListener {
                        trySend(BaseResult.Success(false)).isSuccess
                    }
            }
            awaitClose { listener }
        }

    override fun search(text: String): Flow<BaseResult.Success<List<PostEntity?>>> = callbackFlow {
        val ref = db.collection("Posts")
            .document("Data")
            .collection("List")
            .orderBy("body")
            .startAt(text)
            .endAt(text + "\uf8ff")


        val listener = ref.addSnapshotListener { value, _ ->
            if (value?.documents?.isNotEmpty() == true) {
                val list = value.documents.map { docSnapshot ->
                    docSnapshot.toObject(PostEntity::class.java)
                }

                value.documents.mapIndexed { index, documentSnapshot ->
                    list[index]?.id = documentSnapshot.id
                }

                BaseResult.Success(list).let {
                    trySend(it).isSuccess
                }
            }
        }
        awaitClose { listener.remove() }
    }
}