package com.noplanb.noplanb.data.repository

import android.content.Context
import com.noplanb.noplanb.data.NoPlanBDatabase
import com.noplanb.noplanb.data.dao.ProjectDao
import com.noplanb.noplanb.data.models.Project

class NoPlanBRepository(context: Context) {

    private val db= NoPlanBDatabase.getDatabase(context)
    private val projectDao = db.projectDao()

    // Project
    val getAllProjects =  projectDao.getAllData()

    suspend fun insertProject(project: Project) {
        projectDao.insertData(project)
    }

    suspend fun deleteProject(project: Project) {
        projectDao.deleteItem(project)
    }

    suspend fun updateProject(project: Project) {
        projectDao.updateData(project)
    }

    // Task
}