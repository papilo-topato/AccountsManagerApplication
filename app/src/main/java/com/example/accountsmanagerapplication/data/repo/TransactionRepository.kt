package com.example.accountsmanagerapplication.data.repo

import com.example.accountsmanagerapplication.data.TransactionEntity
import com.example.accountsmanagerapplication.data.dao.ProjectBalanceRow
import com.example.accountsmanagerapplication.data.dao.TransactionDao
import kotlinx.coroutines.flow.Flow

class TransactionRepository(
    private val transactionDao: TransactionDao
) {
    fun observeTransactions(projectId: Long): Flow<List<TransactionEntity>> =
        transactionDao.observeByProject(projectId)

    fun observeTransactionById(id: Long): Flow<TransactionEntity?> = transactionDao.observeById(id)

    suspend fun addIncome(
        projectId: Long,
        amountMinor: Long,
        title: String,
        timestampEpochMs: Long = System.currentTimeMillis(),
        categoryId: Long? = null,
        notes: String? = null
    ): Long {
        val entity = TransactionEntity(
            projectId = projectId,
            timestampEpochMs = timestampEpochMs,
            title = title,
            notes = notes,
            categoryId = categoryId,
            creditAmount = amountMinor,
            debitAmount = 0
        )
        return transactionDao.insert(entity)
    }

    suspend fun addExpense(
        projectId: Long,
        amountMinor: Long,
        title: String,
        timestampEpochMs: Long = System.currentTimeMillis(),
        categoryId: Long? = null,
        notes: String? = null
    ): Long {
        val entity = TransactionEntity(
            projectId = projectId,
            timestampEpochMs = timestampEpochMs,
            title = title,
            notes = notes,
            categoryId = categoryId,
            creditAmount = 0,
            debitAmount = amountMinor
        )
        return transactionDao.insert(entity)
    }

    suspend fun update(entity: TransactionEntity) = transactionDao.update(entity)

    suspend fun delete(entity: TransactionEntity) = transactionDao.delete(entity)

    suspend fun deleteTransactionsForProject(projectId: Long) = transactionDao.deleteByProject(projectId)

    fun observeProjectBalances(): Flow<List<ProjectBalanceRow>> = transactionDao.observeProjectBalances()

    fun observeTransactionsForProject(projectId: Long): Flow<List<TransactionEntity>> =
        transactionDao.observeTransactionsForProject(projectId)

    fun observeAllTransactions(): Flow<List<TransactionEntity>> = transactionDao.observeAll()
}


