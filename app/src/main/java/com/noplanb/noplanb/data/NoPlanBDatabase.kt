package com.noplanb.noplanb.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.noplanb.noplanb.data.dao.ProjectDao
import com.noplanb.noplanb.data.models.Project


@Database(entities = [Project::class], version = 1, exportSchema = false)
abstract class NoPlanBDatabase: RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    companion object {
        @Volatile
        private var INSTANCE: NoPlanBDatabase? = null

        fun getDatabase(context: Context): NoPlanBDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                NoPlanBDatabase::class.java, "npb_database"
            )
                .build()
    }


}