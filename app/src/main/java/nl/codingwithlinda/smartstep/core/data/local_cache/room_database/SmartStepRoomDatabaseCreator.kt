package nl.codingwithlinda.smartstep.core.data.local_cache.room_database

import android.content.Context
import androidx.room.Room

object SmartStepRoomDatabaseCreator {
    @Volatile
    private var dbInstance : SmartStepDatabase? = null

    @Synchronized
    fun getInstance(context: Context): SmartStepDatabase{

        return dbInstance ?: synchronized(this){
            val db = Room.databaseBuilder<SmartStepDatabase>(
                context,
                SmartStepDatabase::class.java,
                SmartStepDatabase.databaseName
            ).fallbackToDestructiveMigration(false)
                .build()


            dbInstance = db
            dbInstance!!

        }
    }
}