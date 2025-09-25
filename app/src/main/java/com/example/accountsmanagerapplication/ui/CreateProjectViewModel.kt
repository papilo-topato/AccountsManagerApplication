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
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    fun onNameChange(value: String) {
        _name.value = value
        // Clear previous error when user starts typing
        _errorMessage.value = null
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
        
        _errorMessage.value = null // Clear previous error
        
        viewModelScope.launch {
            try {
                projectRepository.createProject(currentName, currentDescription)
                // Success - clear form
                resetForm()
            } catch (e: IllegalArgumentException) {
                _errorMessage.value = e.message
            } catch (e: Exception) {
                _errorMessage.value = "Failed to create project: ${e.message}"
            }
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
        
        _errorMessage.value = null // Clear previous error
        
        viewModelScope.launch {
            try {
                val project = ProjectEntity(
                    id = projectId,
                    name = currentName,
                    description = currentDescription
                )
                projectRepository.updateProject(project)
                // Success - clear form
                resetForm()
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update project: ${e.message}"
            }
        }
    }
    
    fun resetForm() {
        _name.value = ""
        _description.value = ""
        _errorMessage.value = null
    }
    
    fun clearError() {
        _errorMessage.value = null
    }
}