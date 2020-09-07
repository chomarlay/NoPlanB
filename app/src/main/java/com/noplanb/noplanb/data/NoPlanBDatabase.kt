package com.noplanb.noplanb.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.noplanb.noplanb.data.dao.ProjectDao
import com.noplanb.noplanb.data.dao.TaskDao
import com.noplanb.noplanb.data.models.Project
import com.noplanb.noplanb.data.models.Task


@Database(entities = [Project::class, Task::class], version = 1, exportSchema = false)
abstract class NoPlanBDatabase: RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun taskDao(): TaskDao
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