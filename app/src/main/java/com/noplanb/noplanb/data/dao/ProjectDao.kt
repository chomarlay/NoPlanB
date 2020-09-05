package com.noplanb.noplanb.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.noplanb.noplanb.data.models.Project

@Dao
interface ProjectDao {
    @Query("SELECT * FROM Project")
    fun getAllData(): LiveData<List<Project>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(project: Project )

    @Update
    suspend fun updateData(project: Project)

    @Delete
    suspend fun deleteItem(project: Project)

    @Query("DELETE FROM Project")
    suspend fun deleteAll()

}