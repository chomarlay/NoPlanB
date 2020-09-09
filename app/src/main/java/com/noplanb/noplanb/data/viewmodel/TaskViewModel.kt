package com.noplanb.noplanb.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.noplanb.noplanb.data.models.Project
import com.noplanb.noplanb.data.models.Task
import com.noplanb.noplanb.data.repository.NoPlanBRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val repository= NoPlanBRepository(application)
//    val getAllProjects: LiveData<List<Project>>
//
//    init {
//        getAllProjects = repository.getAllProjects
//    }

    fun insertTask (task: Task) {
        viewModelScope.launch (Dispatchers.IO) {
            repository.insertTask(task)
        }
    }

    fun updateProject (task: Task) {
        viewModelScope.launch (Dispatchers.IO) {
            repository.updateTask(task)
        }
    }

    fun deleteProject (task: Task) {
        viewModelScope.launch (Dispatchers.IO) {
            repository.deleteTask(task)
        }
    }



}