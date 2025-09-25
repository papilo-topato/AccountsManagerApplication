package com.example.accountsmanagerapplication.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.accountsmanagerapplication.di.AppModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TransactionEditViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {
    private val db = AppModule.provideDatabase(application)
    private val txnRepo = AppModule.provideTransactionRepository(AppModule.provideTransactionDao(db))

    val projectId: Long = checkNotNull(savedStateHandle["projectId"])
    private val transactionTypeArg: String? = savedStateHandle.get<String>("transactionType")?.lowercase()
    private val transactionIdArg: Long? = savedStateHandle.get<Long>("transactionId")
    val isEditMode: Boolean = transactionIdArg != null
    val isIncome: Boolean = when {
        isEditMode -> false // will be determined from loaded entity
        else -> (transactionTypeArg ?: "income") == "income"
    }

    private val _amount = MutableStateFlow("")
    val amount: StateFlow<String> = _amount.asStateFlow()

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title.asStateFlow()

    private val _notes = MutableStateFlow("")
    val notes: StateFlow<String> = _notes.asStateFlow()

    private val _date = MutableStateFlow("")
    val date: StateFlow<String> = _date.asStateFlow()

    fun onAmountChange(value: String) { _amount.value = value }
    fun onTitleChange(value: String) { 
        _title.value = value 
        android.util.Log.d("TransactionEditViewModel", "Title changed to: $value")
    }
    fun onNotesChange(value: String) { _notes.value = value }
    fun onDateChange(value: String) { _date.value = value }

    fun isValid(): Boolean = _amount.value.isNotBlank() && _title.value.isNotBlank() && _date.value.isNotBlank()

    private fun parseAmountMinor(amountString: String): Long? {
        val trimmed = amountString.trim()
        if (trimmed.isBlank()) return null
        return try {
            val normalized = trimmed.replace(",", "")
            val parts = normalized.split('.')
            val rupees = parts.getOrNull(0)?.toLongOrNull() ?: return null
            val paise = when (parts.size) {
                1 -> 0L
                else -> parts[1].padEnd(2, '0').take(2).toLongOrNull() ?: return null
            }
            rupees * 100 + paise
        } catch (e: Exception) {
            null
        }
    }

    private fun parseDateToEpoch(dateString: String): Long? {
        return try {
            val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
            sdf.parse(dateString)?.time
        } catch (e: Exception) {
            null
        }
    }

    init {
        if (isEditMode) {
            val id = checkNotNull(transactionIdArg)
            viewModelScope.launch {
                val entity = txnRepo.observeTransactionById(id).first()
                if (entity != null) {
                    val amt = if (entity.creditAmount > 0) entity.creditAmount else entity.debitAmount
                    _amount.value = String.format("%d.%02d", amt / 100, amt % 100)
                    _title.value = entity.title
                    _notes.value = entity.notes ?: ""
                    // Format date as dd/MM/yyyy
                    val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                    _date.value = sdf.format(java.util.Date(entity.timestampEpochMs))
                }
            }
        } else {
            // Set current date for new transactions
            val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
            _date.value = sdf.format(java.util.Date())
        }
    }

    fun saveTransaction() {
        val amountMinor = parseAmountMinor(_amount.value) ?: return
        val title = _title.value.trim()
        val notes = _notes.value.trim().ifEmpty { null }
        val timestampEpochMs = parseDateToEpoch(_date.value) ?: return
        if (title.isBlank()) return
        viewModelScope.launch {
            if (isEditMode) {
                val id = checkNotNull(transactionIdArg)
                val existing = txnRepo.observeTransactionById(id).first()
                if (existing != null) {
                    val updated = existing.copy(
                        title = title,
                        notes = notes,
                        timestampEpochMs = timestampEpochMs,
                        creditAmount = if (existing.creditAmount > 0) amountMinor else 0,
                        debitAmount = if (existing.debitAmount > 0) amountMinor else 0
                    )
                    txnRepo.update(updated)
                }
            } else {
                if (isIncome) {
                    txnRepo.addIncome(projectId = projectId, amountMinor = amountMinor, title = title, notes = notes, timestampEpochMs = timestampEpochMs)
                } else {
                    txnRepo.addExpense(projectId = projectId, amountMinor = amountMinor, title = title, notes = notes, timestampEpochMs = timestampEpochMs)
                }
            }
        }
    }

    fun deleteTransaction() {
        if (!isEditMode) return
        val id = checkNotNull(transactionIdArg)
        viewModelScope.launch {
            val existing = txnRepo.observeTransactionById(id).first()
            if (existing != null) {
                txnRepo.delete(existing)
            }
        }
    }
}



