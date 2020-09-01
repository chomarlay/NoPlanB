package com.noplanb.noplanb.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.noplanb.noplanb.data.models.Project

@Dao
interface ProjectDao {
    @Query("SELECT * FROM Project")
    fun getAllData(): LiveData<List<Project>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(toDoData: Project )

    @Update
    suspend fun updateData(toDoData: Project)

    @Delete
    suspend fun deleteItem(toDoData: Project)

    @Query("DELETE FROM Project")
    suspend fun deleteAll()

}