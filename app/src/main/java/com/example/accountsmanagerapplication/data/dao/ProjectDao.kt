package com.example.accountsmanagerapplication.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.accountsmanagerapplication.data.ProjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects ORDER BY display_order ASC, created_at_epoch_ms DESC")
    fun observeAll(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE id = :id")
    fun observeById(id: Long): Flow<ProjectEntity?>

    @Query("SELECT * FROM projects WHERE id = :projectId")
    fun observeProject(projectId: Long): Flow<ProjectEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: ProjectEntity): Long

    @Update
    suspend fun update(entity: ProjectEntity)

    @Delete
    suspend fun delete(entity: ProjectEntity)

    @Query("DELETE FROM projects")
    suspend fun deleteAll()
    
    @Query("UPDATE projects SET display_order = :order WHERE id = :projectId")
    suspend fun updateDisplayOrder(projectId: Long, order: Int)
    
    @Query("SELECT * FROM projects WHERE id = :id")
    suspend fun getProjectById(id: Long): ProjectEntity?
    
    @Query("SELECT * FROM projects WHERE name = :name LIMIT 1")
    suspend fun getProjectByName(name: String): ProjectEntity?
    
    @Query("SELECT * FROM projects WHERE name = :name AND id != :excludeId LIMIT 1")
    suspend fun findProjectByNameExcludingId(name: String, excludeId: Long): ProjectEntity?
    
    @Update
    suspend fun updateProjects(projects: List<ProjectEntity>)
    
    @Query("UPDATE projects SET display_order = display_order + 1")
    suspend fun incrementAllDisplayOrders()
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ProjectEntity): Long
    
    @Transaction
    suspend fun insertProjectAtTop(project: ProjectEntity): Long {
        incrementAllDisplayOrders()
        // The project entity being passed in should already have its displayOrder set to 0
        return insertProject(project)
    }
}


