package com.noplanb.noplanb.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.noplanb.noplanb.data.NoPlanBDatabase
import com.noplanb.noplanb.data.models.Project
import com.noplanb.noplanb.data.models.ProjectWithTasks
import com.noplanb.noplanb.data.models.Task
import com.noplanb.noplanb.data.models.TaskWithProject
import java.util.*

class NoPlanBRepository(context: Context) {

    private val db= NoPlanBDatabase.getDatabase(context)
    private val projectDao = db.projectDao()
    private val taskDao = db.taskDao()

    // Project
    val getAllProjects =  projectDao.getAllData()
    val getProjectsWithTasks = projectDao.getProjectsWithTasks()

    fun getProjectWithTasks(projectId: Int): LiveData<ProjectWithTasks> {
        return projectDao.getProjectWithTasks(projectId)
    }

    fun getProjectById(id: Int): LiveData<Project> {
        return projectDao.getProjectById(id)
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

    fun getTasksDueBeforeDate(beforeDate: Date): LiveData<List<TaskWithProject>> {
        return taskDao.getTasksByBeforeDate(beforeDate)
    }

    fun getTasksByProject(projectId: Int): LiveData<List<TaskWithProject>> {
        return taskDao.getTasksByProject(projectId)
    }
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