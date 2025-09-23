package com.example.accountsmanagerapplication.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.accountsmanagerapplication.data.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions WHERE project_id = :projectId ORDER BY timestamp_epoch_ms DESC")
    fun observeByProject(projectId: Long): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE project_id = :projectId ORDER BY timestamp_epoch_ms DESC")
    fun observeTransactionsForProject(projectId: Long): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    fun observeById(id: Long): Flow<TransactionEntity?>

    @Query("SELECT * FROM transactions")
    fun observeAll(): Flow<List<TransactionEntity>>

    @Query(
        "SELECT p.id AS projectId, p.name AS name, p.description AS description, " +
            "IFNULL(SUM(t.credit_amount), 0) - IFNULL(SUM(t.debit_amount), 0) AS balance " +
            "FROM projects p LEFT JOIN transactions t ON p.id = t.project_id " +
            "GROUP BY p.id ORDER BY p.created_at_epoch_ms DESC"
    )
    fun observeProjectBalances(): Flow<List<ProjectBalanceRow>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(entity: TransactionEntity): Long

    @Update
    suspend fun update(entity: TransactionEntity)

    @Delete
    suspend fun delete(entity: TransactionEntity)

    @Query("DELETE FROM transactions WHERE project_id = :projectId")
    suspend fun deleteByProject(projectId: Long)
}


