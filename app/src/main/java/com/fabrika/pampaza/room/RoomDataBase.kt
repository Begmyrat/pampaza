package com.fabrika.pampaza.room

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fabrika.pampaza.room.dao.PostDAO

abstract class RoomDataBase: RoomDatabase() {

    abstract fun postDAO(): PostDAO

    companion object{
        @Volatile
        private var INSTANCE: RoomDataBase? = null

        fun getDatabase(context: Context): RoomDataBase {
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomDataBase::class.java,
                    "room_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }

    }
}
