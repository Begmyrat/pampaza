package com.fabrika.pampaza.firebase

import com.fabrika.pampaza.common.model.UserModel
import com.fabrika.pampaza.common.utils.BaseResult
import com.fabrika.pampaza.common.utils.DomainError
import com.fabrika.pampaza.home.model.PostEntity
import com.fabrika.pampaza.login.model.UserEntity
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import java.util.*

class FirebaseRepository: FirebaseRepositoryImpl {

    val db = Firebase.firestore
    override fun getAllPosts(): Flow<BaseResult.Success<List<PostEntity?>>> = callbackFlow{
        val ref = db.collection("Posts")
            .document("Data")
            .collection("List")
            .orderBy("id", Query.Direction.DESCENDING)

        val listener = ref.addSnapshotListener { value, error ->
            if(value?.documents?.isNotEmpty() == true){
                val list = value.documents.map { docSnapshot ->
                    docSnapshot.toObject(PostEntity::class.java)
                }
                BaseResult.Success(list).let {
                    trySend(it).isSuccess
                }
            }
        }
        awaitClose{ listener.remove() }
    }

    override fun post(
        body: String,
        imageUrl: String?,
        originalPostId: String?
    ): Flow<BaseResult.Success<Boolean>> = callbackFlow{
        val milliseconds = Calendar.getInstance().timeInMillis
        val ref = db.collection("Posts")
            .document("Data")
            .collection("List")
            .add(
                hashMapOf(
                    "authorAvatarUrl" to "authorAvatarUrlValue",
                    "authorId" to "authorIdValue",
                    "authorName" to "authorNameValue",
                    "body" to body,
                    "commentCount" to 0,
                    "complaintCount" to 0,
                    "date" to milliseconds,
                    "id" to milliseconds,
                    "imageUrl" to imageUrl,
                    "likeCount" to 0,
                    "originalPostId" to originalPostId,
                    "rePostCount" to 0
                )
            )

            val listener = ref.addOnSuccessListener {
                trySend(BaseResult.Success(true)).isSuccess
            }.addOnFailureListener {
                trySend(BaseResult.Success(false)).isSuccess
            }

        awaitClose { listener }
    }

    override fun getUser(username: String, password: String): Flow<BaseResult.Success<UserEntity>> = callbackFlow{
        val ref = db.collection("Users")
            .document("Data")
            .collection("List")
            .whereEqualTo("userId", username)

        val listener = ref.addSnapshotListener { value, error ->
            if(value?.documents?.isNotEmpty() == true){
                val list = value.documents.map { docSnapshot ->
                    docSnapshot.toObject(UserEntity::class.java)
                }

                list.firstOrNull().let {
                    BaseResult.Success(it!!).let { result ->
                        trySend(result).isSuccess
                    }
                }
            } else{
                trySend(BaseResult.Success(UserEntity())).isSuccess
            }
        }
        awaitClose{ listener.remove() }
    }
}