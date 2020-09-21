package com.noplanb.noplanb.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.noplanb.noplanb.data.models.Task
import com.noplanb.noplanb.data.models.TaskWithProject
import java.util.*

@Dao
interface TaskDao {
    @Query("SELECT * FROM Task")
    fun getAllData(): LiveData<List<Task>>

    @Transaction
    @Query("SELECT * FROM Task where projectId= :projectId order by dueDate")
    fun getTasksByProject(projectId: Int): LiveData<List<TaskWithProject>>

    @Transaction
    @Query("SELECT * FROM Task where dueDate < :beforeDate order by dueDate asc")
    fun getTasksByBeforeDate(beforeDate: Date): LiveData<List<TaskWithProject>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(task: Task)

    @Update
    suspend fun updateData(task: Task)

    @Delete
    suspend fun deleteItem(task: Task)

    @Query("DELETE FROM Task")
    suspend fun deleteAll()
}