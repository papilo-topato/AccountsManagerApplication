package com.example.accountsmanagerapplication.data.repo

import com.example.accountsmanagerapplication.data.ProjectEntity
import com.example.accountsmanagerapplication.data.dao.ProjectBalanceRow
import com.example.accountsmanagerapplication.data.dao.ProjectDao
import kotlinx.coroutines.flow.Flow

class ProjectRepository(
    private val projectDao: ProjectDao
) {
    fun observeProjects(): Flow<List<ProjectEntity>> = projectDao.observeAll()

    fun observeProjectById(id: Long): Flow<ProjectEntity?> = projectDao.observeById(id)

    fun observeProjectBalances(transactionRepository: com.example.accountsmanagerapplication.data.repo.TransactionRepository): Flow<List<ProjectBalanceRow>> =
        transactionRepository.observeProjectBalances()

    fun observeProject(projectId: Long): Flow<ProjectEntity> = projectDao.observeProject(projectId)

    suspend fun createProject(name: String, description: String?): Long {
        val entity = ProjectEntity(
            name = name.trim(),
            description = description?.trim(),
            // CRITICAL: Set the displayOrder to 0 for the new top item
            displayOrder = 0,
            createdAtEpochMs = System.currentTimeMillis()
        )
        return projectDao.insertProjectAtTop(entity)
    }

    suspend fun updateProject(entity: ProjectEntity) {
        projectDao.update(entity)
    }

    suspend fun deleteProject(entity: ProjectEntity) {
        projectDao.delete(entity)
    }
    
    suspend fun updateProjectOrder(projectId: Long, order: Int) {
        projectDao.updateDisplayOrder(projectId, order)
    }
    
    suspend fun getProjectById(id: Long): ProjectEntity? {
        return projectDao.getProjectById(id)
    }
    
    suspend fun updateProjects(projects: List<ProjectEntity>) {
        projectDao.updateProjects(projects)
    }
    
    suspend fun getProjectByName(name: String): ProjectEntity? {
        return projectDao.getProjectByName(name)
    }
    
    suspend fun findProjectByNameExcludingId(name: String, excludeId: Long): ProjectEntity? {
        return projectDao.findProjectByNameExcludingId(name, excludeId)
    }
}


