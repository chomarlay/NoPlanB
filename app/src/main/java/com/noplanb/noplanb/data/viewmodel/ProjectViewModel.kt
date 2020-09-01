package com.noplanb.noplanb.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.noplanb.noplanb.data.models.Project
import com.noplanb.noplanb.data.repository.NoPlanBRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProjectViewModel( application: Application) : AndroidViewModel(application) {
    private val repository= NoPlanBRepository(application)
    val getAllProjects = repository.getAllProjects

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