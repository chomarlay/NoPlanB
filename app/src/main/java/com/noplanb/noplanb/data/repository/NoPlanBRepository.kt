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

    fun getAllProjectsWithTasks(): LiveData<List<ProjectWithTasks>> {
        return projectDao.getAllProjectsWithTasks()
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
    //get non completed tasks due before a particular date
    fun getTasksDueBeforeDate(beforeDate: Date): LiveData<List<TaskWithProject>> {
        return taskDao.getTasksByBeforeDate(beforeDate)
    }

    // get tasks that are not marked as completed
    fun getTasksByProject(projectId: Int): LiveData<List<TaskWithProject>> {
        return taskDao.getTasksByProject(projectId)
    }

    // get all tasks including completed tasks
    fun getAllTasksByProject(projectId: Int): LiveData<List<TaskWithProject>> {
        return taskDao.getAllTasksByProject(projectId)
    }

    //get non completed tasks due before a particular date and title
    fun getTasksDueBeforeDateAndTitle(beforeDate: Date, title: String): LiveData<List<TaskWithProject>> {
        return taskDao.getTasksByBeforeDateAndTitle(beforeDate, title)
    }

    // get tasks that are not marked as completed and title
    fun getTasksByProjectAndTitle(projectId: Int, title: String): LiveData<List<TaskWithProject>> {
        return taskDao.getTasksByProjectAndTitle(projectId, title)
    }

    // get all tasks including completed tasks and title
    fun getAllTasksByProjectAndTitle(projectId: Int, title: String): LiveData<List<TaskWithProject>> {
        return taskDao.getAllTasksByProjectAndTitle(projectId, title)
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