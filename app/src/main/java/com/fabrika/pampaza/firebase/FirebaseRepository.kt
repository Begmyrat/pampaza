package com.fabrika.pampaza.firebase

import com.fabrika.pampaza.common.model.UserModel
import com.fabrika.pampaza.common.utils.BaseResult
import com.fabrika.pampaza.home.model.PostEntity
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

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

    fun getAllPosts(): Flow<BaseResult.Success<PostEntity>>{

    }

}