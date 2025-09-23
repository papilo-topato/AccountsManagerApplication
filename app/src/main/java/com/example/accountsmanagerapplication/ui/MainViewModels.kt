package com.example.accountsmanagerapplication.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.accountsmanagerapplication.data.ProjectEntity
import com.example.accountsmanagerapplication.data.dao.ProjectBalanceRow
import com.example.accountsmanagerapplication.data.repo.ProjectRepository
import com.example.accountsmanagerapplication.data.repo.TransactionRepository
import com.example.accountsmanagerapplication.di.AppModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProjectListViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppModule.provideDatabase(application)
    private val projectRepository = AppModule.provideProjectRepository(AppModule.provideProjectDao(db))
    private val transactionRepository = AppModule.provideTransactionRepository(AppModule.provideTransactionDao(db))

    val projects: StateFlow<List<ProjectEntity>> =
        projectRepository.observeProjects()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val projectBalances: StateFlow<List<ProjectBalanceRow>> =
        transactionRepository.observeProjectBalances()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun addProject(name: String, description: String?) {
        viewModelScope.launch {
            projectRepository.createProject(name, description)
        }
    }

    suspend fun prepareAllProjectsExportData(): Pair<List<ProjectBalanceRow>, Map<Long, List<com.example.accountsmanagerapplication.data.TransactionEntity>>> {
        val balances = projectBalances.value
        val allTxns = transactionRepository.observeAllTransactions().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList()).value
        val grouped = allTxns.groupBy { it.projectId }
        return balances to grouped
    }
}

data class ProjectSummary(
    val project: ProjectEntity,
    val totalCredit: Long,
    val totalDebit: Long,
    val balance: Long
)

class ProjectDetailViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {
    private val db = AppModule.provideDatabase(application)
    private val projectRepository = AppModule.provideProjectRepository(AppModule.provideProjectDao(db))
    private val transactionRepository = AppModule.provideTransactionRepository(AppModule.provideTransactionDao(db))

    private val projectId: Long = checkNotNull(savedStateHandle["projectId"])

    val project: StateFlow<ProjectEntity?> =
        projectRepository.observeProjectById(projectId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val transactions: StateFlow<List<com.example.accountsmanagerapplication.data.TransactionEntity>> =
        transactionRepository.observeTransactionsForProject(projectId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val totalCredit: StateFlow<Long> =
        transactions.map { it.sumOf { t -> t.creditAmount } }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0L)

    suspend fun prepareSingleProjectExportData(): Pair<ProjectEntity?, List<com.example.accountsmanagerapplication.data.TransactionEntity>> {
        val proj = projectRepository.observeProjectById(projectId).stateIn(viewModelScope).value
        val txns = transactionRepository.observeTransactionsForProject(projectId).stateIn(viewModelScope).value
        return Pair(proj, txns)
    }

    val totalDebit: StateFlow<Long> =
        transactions.map { it.sumOf { t -> t.debitAmount } }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0L)

    val runningBalance: StateFlow<Long> =
        transactions.map { list -> list.sumOf { it.creditAmount - it.debitAmount } }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0L)
}


