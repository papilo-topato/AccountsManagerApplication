package com.example.accountsmanagerapplication.data.repo

import com.example.accountsmanagerapplication.data.DeletedProjectEntity
import com.example.accountsmanagerapplication.data.ProjectEntity
import com.example.accountsmanagerapplication.data.dao.DeletedProjectDao
import kotlinx.coroutines.flow.Flow

class DeletedProjectRepository(
    private val deletedProjectDao: DeletedProjectDao
) {
    fun observeDeletedProjects(): Flow<List<DeletedProjectEntity>> = deletedProjectDao.observeAll()

    suspend fun moveToTrash(project: ProjectEntity) {
        val deletedProject = DeletedProjectEntity(
            originalId = project.id,
            name = project.name,
            description = project.description,
            createdAtEpochMs = project.createdAtEpochMs
        )
        deletedProjectDao.insert(deletedProject)
    }

    suspend fun restoreFromTrash(deletedProject: DeletedProjectEntity): ProjectEntity {
        val restoredProject = ProjectEntity(
            id = deletedProject.originalId,
            name = deletedProject.name,
            description = deletedProject.description,
            createdAtEpochMs = deletedProject.createdAtEpochMs
        )
        deletedProjectDao.delete(deletedProject)
        return restoredProject
    }

    suspend fun permanentlyDelete(deletedProject: DeletedProjectEntity) {
        deletedProjectDao.delete(deletedProject)
    }

    suspend fun cleanupOldDeletedProjects() {
        val thirtyDaysAgo = System.currentTimeMillis() - (30 * 24 * 60 * 60 * 1000L)
        deletedProjectDao.deleteOlderThan(thirtyDaysAgo)
    }
}

