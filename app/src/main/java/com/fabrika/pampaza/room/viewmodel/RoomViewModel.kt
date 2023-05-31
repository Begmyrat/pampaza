package com.fabrika.pampaza.room.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.fabrika.pampaza.room.PostRoomRepository
import com.fabrika.pampaza.room.RoomDataBase
import com.fabrika.pampaza.room.model.PostModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoomViewModel(application: Application): AndroidViewModel(application) {

    private val repository: PostRoomRepository
    var readAll: LiveData<List<PostModel>>
    init {
        val postDB = RoomDataBase.getDatabase(application).postDAO()
        repository = PostRoomRepository(postDB)
        readAll = repository.getAllPosts()
    }

    fun addPost(postModel: PostModel){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertPost(postModel)
        }
    }
}