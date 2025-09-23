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
        val entity = ProjectEntity(name = name, description = description)
        return projectDao.insert(entity)
    }

    suspend fun updateProject(entity: ProjectEntity) {
        projectDao.update(entity)
    }

    suspend fun deleteProject(entity: ProjectEntity) {
        projectDao.delete(entity)
    }
}


