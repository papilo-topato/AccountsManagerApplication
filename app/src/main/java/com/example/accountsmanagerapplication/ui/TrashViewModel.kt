package com.example.accountsmanagerapplication.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.accountsmanagerapplication.data.DeletedProjectEntity
import com.example.accountsmanagerapplication.data.ProjectEntity
import com.example.accountsmanagerapplication.data.repo.DeletedProjectRepository
import com.example.accountsmanagerapplication.data.repo.ProjectRepository
import com.example.accountsmanagerapplication.di.AppModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TrashViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppModule.provideDatabase(application)
    private val deletedProjectRepository = AppModule.provideDeletedProjectRepository(
        AppModule.provideDeletedProjectDao(db)
    )
    private val projectRepository = AppModule.provideProjectRepository(
        AppModule.provideProjectDao(db)
    )

    val deletedProjects: StateFlow<List<DeletedProjectEntity>> =
        deletedProjectRepository.observeDeletedProjects()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun restoreProject(deletedProject: DeletedProjectEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val restoredProject = deletedProjectRepository.restoreFromTrash(deletedProject)
                projectRepository.createProject(restoredProject.name, restoredProject.description)
            } catch (e: Exception) {
                // Handle error - could show a toast or snackbar
                android.util.Log.e("TrashViewModel", "Failed to restore project", e)
            }
        }
    }

    fun permanentlyDeleteProject(deletedProject: DeletedProjectEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                deletedProjectRepository.permanentlyDelete(deletedProject)
            } catch (e: Exception) {
                // Handle error - could show a toast or snackbar
                android.util.Log.e("TrashViewModel", "Failed to permanently delete project", e)
            }
        }
    }

    fun cleanupOldDeletedProjects() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                deletedProjectRepository.cleanupOldDeletedProjects()
            } catch (e: Exception) {
                // Handle error - could show a toast or snackbar
                android.util.Log.e("TrashViewModel", "Failed to cleanup old deleted projects", e)
            }
        }
    }
}

