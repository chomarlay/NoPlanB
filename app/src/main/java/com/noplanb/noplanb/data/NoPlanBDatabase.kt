package com.noplanb.noplanb.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.noplanb.noplanb.data.dao.ProjectDao
import com.noplanb.noplanb.data.dao.TaskDao
import com.noplanb.noplanb.data.models.Project
import com.noplanb.noplanb.data.models.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Database(entities = [Project::class, Task::class], version = 1, exportSchema = false)
@TypeConverters (DateTypeConverter::class)
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
            )   .fallbackToDestructiveMigration()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        GlobalScope.launch(Dispatchers.Main) {
                            val noPlanBProject = Project(0,"NoPlanB", "Believe Effort Action Result", null)
                            INSTANCE?.projectDao()?.insertData(noPlanBProject)
                        }
                        super.onCreate(db)

                    }
                }).build()
    }


}