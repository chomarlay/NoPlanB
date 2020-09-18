package com.noplanb.noplanb.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.noplanb.noplanb.data.models.Project
import com.noplanb.noplanb.data.models.ProjectWithTasks
import com.noplanb.noplanb.data.repository.NoPlanBRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProjectViewModel( application: Application) : AndroidViewModel(application) {
    private val repository= NoPlanBRepository(application)
    val getAllProjects: LiveData<List<Project>>

    init {
        getAllProjects = repository.getAllProjects
    }

    fun getProjectWithTasks(projectId: Int): LiveData<ProjectWithTasks> {
        return repository.getProjectWithTasks(projectId)
    }

    fun getProjectById(projectId: Int): LiveData<Project> {
         return repository.getProjectById(projectId)
    }

    fun insertProject (project: Project) {
        viewModelScope.launch (Dispatchers.IO) {
            repository.insertProject(project)
        }
    }

    fun updateProject (project: Project) {
        viewModelScope.launch (Dispatchers.IO) {
            repository.updateProject(project)
        }
    }

    fun deleteProject (project: Project) {
        viewModelScope.launch (Dispatchers.IO) {
            repository.deleteProject(project)
        }
    }



}