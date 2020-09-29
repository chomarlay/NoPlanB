package com.noplanb.noplanb.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.noplanb.noplanb.data.models.Project
import com.noplanb.noplanb.data.models.Task
import com.noplanb.noplanb.data.models.TaskWithProject
import com.noplanb.noplanb.data.repository.NoPlanBRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val repository= NoPlanBRepository(application)

    fun getTasksByProject(projectId: Int): LiveData<List<TaskWithProject>> {
        return repository.getTasksByProject(projectId)
    }

    fun getAllTasksByProject(projectId: Int): LiveData<List<TaskWithProject>> {
        return repository.getAllTasksByProject(projectId)
    }
    fun getTasksDueBeforeDate(beforeDate: Date): LiveData<List<TaskWithProject>> {
        return repository.getTasksDueBeforeDate(beforeDate)
    }

    //get non completed tasks due before a particular date and title
    fun getTasksDueBeforeDateAndTitle(beforeDate: Date, title: String): LiveData<List<TaskWithProject>> {
        return repository.getTasksDueBeforeDateAndTitle(beforeDate, title)
    }

    // get tasks that are not marked as completed and title
    fun getTasksByProjectAndTitle(projectId: Int, title: String): LiveData<List<TaskWithProject>> {
        return repository.getTasksByProjectAndTitle(projectId, title)
    }

    // get all tasks including completed tasks and title
    fun getAllTasksByProjectAndTitle(projectId: Int, title: String): LiveData<List<TaskWithProject>> {
        return repository.getAllTasksByProjectAndTitle(projectId, title)
    }

    fun insertTask (task: Task) {
        viewModelScope.launch (Dispatchers.IO) {
            repository.insertTask(task)
        }
    }

    fun updateTask (task: Task) {
        viewModelScope.launch (Dispatchers.IO) {
            repository.updateTask(task)
        }
    }

    fun deleteTask (task: Task) {
        viewModelScope.launch (Dispatchers.IO) {
            repository.deleteTask(task)
        }
    }



}