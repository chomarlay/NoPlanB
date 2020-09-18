package com.noplanb.noplanb.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.noplanb.noplanb.data.models.Project
import com.noplanb.noplanb.data.models.ProjectWithTasks

@Dao
interface ProjectDao {

    @Query("SELECT * FROM Project")
    fun getAllData(): LiveData<List<Project>>

    @Transaction
    @Query("SELECT * FROM Project")
    fun getProjectsWithTasks(): LiveData<List<ProjectWithTasks>>

    @Transaction
    @Query("SELECT * FROM Project where id=:projectId")
    fun getProjectWithTasks(projectId: Int): LiveData<ProjectWithTasks>

    @Query("SELECT * FROM Project where id = :id")
    fun getProjectById(id: Int): LiveData<Project>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(project: Project )

    @Update
    suspend fun updateData(project: Project)

    @Delete
    suspend fun deleteItem(project: Project)

    @Query("DELETE FROM Project")
    suspend fun deleteAll()

}