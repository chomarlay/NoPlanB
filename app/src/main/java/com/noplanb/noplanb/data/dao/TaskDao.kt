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
    @Query("SELECT * FROM Task where projectId= :projectId and completedDate is null order by dueDate, title")
    fun getTasksByProject(projectId: Int): LiveData<List<TaskWithProject>>

    @Transaction
    @Query("SELECT * FROM Task where projectId= :projectId order by dueDate, title")
    fun getAllTasksByProject(projectId: Int): LiveData<List<TaskWithProject>>

    @Transaction
    @Query("SELECT * FROM Task where dueDate < :beforeDate and completedDate is null order by dueDate, title")
    fun getTasksByBeforeDate(beforeDate: Date): LiveData<List<TaskWithProject>>

    @Transaction
    @Query("SELECT * FROM Task where projectId= :projectId and title like :title and completedDate is null order by dueDate, title")
    fun getTasksByProjectAndTitle(projectId: Int, title: String): LiveData<List<TaskWithProject>>

    @Transaction
    @Query("SELECT * FROM Task where projectId= :projectId and title like :title order by dueDate, title")
    fun getAllTasksByProjectAndTitle(projectId: Int, title: String): LiveData<List<TaskWithProject>>

    @Transaction
    @Query("SELECT * FROM Task where dueDate < :beforeDate and title like :title and completedDate is null order by dueDate, title")
    fun getTasksByBeforeDateAndTitle(beforeDate: Date, title: String): LiveData<List<TaskWithProject>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(task: Task)

    @Update
    suspend fun updateData(task: Task)

    @Delete
    suspend fun deleteItem(task: Task)

    @Query("DELETE FROM Task")
    suspend fun deleteAll()
}