package com.fabrika.pampaza.firebase

import com.fabrika.pampaza.common.model.UserModel
import com.fabrika.pampaza.common.utils.BaseResult
import com.fabrika.pampaza.home.model.PostEntity
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

class FirebaseRepository {

    val db = Firebase.firestore

    fun getUserData(id: String): Flow<BaseResult.Success<UserModel>> = callbackFlow {
        val res = db.collection("Users")
            .document(id)

        val subscription = res.addSnapshotListener{ value, error ->

            val userObj = value?.toObject(UserModel::class.java)

//            BaseResult.Success(userObj)
//                .let { trySend(it).isSuccess }
        }

        awaitClose { subscription.remove() }
    }

    fun getAllPosts(): Flow<BaseResult.Success<List<PostEntity?>>> = callbackFlow{
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

    fun post(
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

}