package com.noplanb.noplanb.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.noplanb.noplanb.data.models.Task

@Dao
interface TaskDao {
    @Query("SELECT * FROM Task")
    fun getAllData(): LiveData<List<Task>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(task: Task)

    @Update
    suspend fun updateData(task: Task)

    @Delete
    suspend fun deleteItem(task: Task)

    @Query("DELETE FROM Task")
    suspend fun deleteAll()
}