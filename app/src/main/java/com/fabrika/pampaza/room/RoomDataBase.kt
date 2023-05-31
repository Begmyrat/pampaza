package com.fabrika.pampaza.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fabrika.pampaza.room.dao.PostDAO
import com.fabrika.pampaza.room.model.PostModel

@Database(
    entities = [PostModel::class],
    version = 2
)
abstract class RoomDataBase : RoomDatabase() {

    abstract fun postDAO(): PostDAO

    companion object {
        @Volatile
        private var INSTANCE: RoomDataBase? = null

        fun getDatabase(context: Context): RoomDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room
                    .databaseBuilder(
                        context.applicationContext,
                        RoomDataBase::class.java,
                        "room_database"
                    )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }

    }
}
