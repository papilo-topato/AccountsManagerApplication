package com.example.accountsmanagerapplication.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.accountsmanagerapplication.data.DeletedProjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DeletedProjectDao {
    @Query("SELECT * FROM deleted_projects ORDER BY deleted_at_epoch_ms DESC")
    fun observeAll(): Flow<List<DeletedProjectEntity>>

    @Query("SELECT * FROM deleted_projects WHERE original_id = :originalId")
    suspend fun getByOriginalId(originalId: Long): DeletedProjectEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(entity: DeletedProjectEntity): Long

    @Delete
    suspend fun delete(entity: DeletedProjectEntity)

    @Query("DELETE FROM deleted_projects WHERE deleted_at_epoch_ms < :cutoffTime")
    suspend fun deleteOlderThan(cutoffTime: Long)

    @Query("DELETE FROM deleted_projects")
    suspend fun deleteAll()
}

