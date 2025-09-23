package com.example.accountsmanagerapplication.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.accountsmanagerapplication.data.ProjectEntity
import com.example.accountsmanagerapplication.di.AppModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CreateProjectViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppModule.provideDatabase(application)
    private val projectRepository = AppModule.provideProjectRepository(AppModule.provideProjectDao(db))

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description.asStateFlow()

    fun onNameChange(value: String) {
        _name.value = value
    }

    fun onDescriptionChange(value: String) {
        _description.value = value
    }

    val isValid: Boolean
        get() = _name.value.isNotBlank()

    fun saveProject() {
        val currentName = _name.value.trim()
        val currentDescription = _description.value.trim().ifEmpty { null }
        if (currentName.isBlank()) return
        viewModelScope.launch {
            projectRepository.createProject(currentName, currentDescription)
        }
    }

    fun setProjectData(name: String, description: String) {
        _name.value = name
        _description.value = description
    }

    fun updateProject(projectId: Long) {
        val currentName = _name.value.trim()
        val currentDescription = _description.value.trim().ifEmpty { null }
        if (currentName.isBlank()) return
        viewModelScope.launch {
            val project = ProjectEntity(
                id = projectId,
                name = currentName,
                description = currentDescription
            )
            projectRepository.updateProject(project)
        }
    }
}



