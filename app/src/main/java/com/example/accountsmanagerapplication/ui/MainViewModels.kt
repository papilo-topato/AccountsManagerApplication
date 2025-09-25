package com.example.accountsmanagerapplication.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.accountsmanagerapplication.data.ProjectEntity
import com.example.accountsmanagerapplication.data.dao.ProjectBalanceRow
import com.example.accountsmanagerapplication.data.repo.ProjectRepository
import com.example.accountsmanagerapplication.data.repo.TransactionRepository
import com.example.accountsmanagerapplication.data.repo.DeletedProjectRepository
import com.example.accountsmanagerapplication.di.AppModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.example.accountsmanagerapplication.util.formatCurrencyMinor

class ProjectListViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppModule.provideDatabase(application)
    private val projectRepository = AppModule.provideProjectRepository(AppModule.provideProjectDao(db))
    private val transactionRepository = AppModule.provideTransactionRepository(AppModule.provideTransactionDao(db))
    private val deletedProjectRepository = AppModule.provideDeletedProjectRepository(AppModule.provideDeletedProjectDao(db))

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

    fun moveProjectToTrash(project: ProjectEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // First, delete all transactions for this project
                transactionRepository.deleteTransactionsForProject(project.id)
                // Then move the project to trash
                deletedProjectRepository.moveToTrash(project)
                // Finally, delete the project from the main table
                projectRepository.deleteProject(project)
            } catch (e: Exception) {
                android.util.Log.e("ProjectListViewModel", "Failed to move project to trash", e)
            }
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

enum class SearchFilter { TITLE, AMOUNT }


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

    // Search functionality
    private val _isSearchActive = MutableStateFlow(false)
    val isSearchActive: StateFlow<Boolean> = _isSearchActive.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchFilter = MutableStateFlow(SearchFilter.TITLE)
    val searchFilter: StateFlow<SearchFilter> = _searchFilter.asStateFlow()

    // Filtered transactions based on search
    val filteredTransactions: StateFlow<List<com.example.accountsmanagerapplication.data.TransactionEntity>> =
        combine(
            transactions,
            searchQuery,
            searchFilter,
            isSearchActive
        ) { transactionList, query, filter, isActive ->
            if (!isActive || query.isBlank()) {
                transactionList
            } else {
                when (filter) {
                    SearchFilter.TITLE -> transactionList.filter {
                        it.title.contains(query, ignoreCase = true)
                    }
                    SearchFilter.AMOUNT -> transactionList.filter {
                        val formattedCredit = formatCurrencyMinor(it.creditAmount)
                        val formattedDebit = formatCurrencyMinor(it.debitAmount)
                        formattedCredit.contains(query) || formattedDebit.contains(query)
                    }
                }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Search control functions
    fun onToggleSearch() {
        _isSearchActive.value = !_isSearchActive.value
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun setSearchFilter(filter: SearchFilter) {
        _searchFilter.value = filter
    }
}


