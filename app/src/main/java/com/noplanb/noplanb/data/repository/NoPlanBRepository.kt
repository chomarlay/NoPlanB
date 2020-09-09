package com.noplanb.noplanb.data.repository

import android.content.Context
import com.noplanb.noplanb.data.NoPlanBDatabase
import com.noplanb.noplanb.data.models.Project
import com.noplanb.noplanb.data.models.Task

class NoPlanBRepository(context: Context) {

    private val db= NoPlanBDatabase.getDatabase(context)
    private val projectDao = db.projectDao()
    private val taskDao = db.taskDao()

    // Project
    val getAllProjects =  projectDao.getAllData()
    val getAllProjectsWithTasks = projectDao.getProjectsWithTasks()

    fun getProjectWithTasks(projectId: Int)  {
        projectDao.getProjectWithTasks(projectId)
    }

    suspend fun getProjects(): List<Project> {
        return projectDao.getProjects()
    }

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
    val getAllTasks = taskDao.getAllData()

    suspend fun insertTask(task: Task){
        taskDao.insertData(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteItem(task)
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateData(task)
    }
}