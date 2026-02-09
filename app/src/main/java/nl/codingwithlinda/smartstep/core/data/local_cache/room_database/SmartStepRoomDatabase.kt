package nl.codingwithlinda.smartstep.core.data.local_cache.room_database

import android.app.Application
import androidx.room.Room

class SmartStepRoomDatabase(
    private val context: Application

) {

    val db = Room.databaseBuilder<SmartStepDatabase>(
        context,
        SmartStepDatabase::class.java,
        SmartStepDatabase.databaseName
    ).fallbackToDestructiveMigration(false)
        .build()

}