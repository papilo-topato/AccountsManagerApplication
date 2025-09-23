package com.example.accountsmanagerapplication.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.window.Dialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.accountsmanagerapplication.ui.theme.SuccessGreen
import com.example.accountsmanagerapplication.ui.theme.ErrorRed
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.example.accountsmanagerapplication.di.AppModule
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionDialog(
    onDismissRequest: () -> Unit,
    transactionType: String, // "income" or "expense"
    projectId: Long
) {
    // Local state management
    var amount by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    
    val isIncome = transactionType == "income"
    val dialogTitle = if (isIncome) "Add Credit / Income" else "Add Debit / Expense"
    val buttonColor = if (isIncome) SuccessGreen else ErrorRed
    
    // Date and time state
    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var useCustomTime by remember { mutableStateOf(false) }
    
    // Calculator state
    val calculatorViewModel = remember { CalculatorViewModel() }
    val calculatorState by calculatorViewModel.state.collectAsState()
    var showCalculator by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }
    val isAmountFieldFocused by interactionSource.collectIsFocusedAsState()
    
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    
    // Validation
    val isValid = amount.isNotBlank() && title.isNotBlank()
    
    // Transaction repository
    val context = LocalContext.current
    val db = AppModule.provideDatabase(context)
    val txnRepo = AppModule.provideTransactionRepository(AppModule.provideTransactionDao(db))
    
    // State for saving
    var isSaving by remember { mutableStateOf(false) }
    
    // Helper function to parse amount
    fun parseAmountMinor(amountString: String): Long? {
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
    
    // Save transaction function
    fun saveTransaction() {
        val amountMinor = parseAmountMinor(amount)
        if (amountMinor != null && title.isNotBlank() && !isSaving) {
            isSaving = true
        }
    }
    
    // Show calculator when amount field is focused
    LaunchedEffect(isAmountFieldFocused) {
        if (isAmountFieldFocused) {
            showCalculator = true
        }
    }

    // Handle the actual saving in a coroutine
    LaunchedEffect(isSaving) {
        if (isSaving) {
            val amountMinor = parseAmountMinor(amount)
            if (amountMinor != null && title.isNotBlank()) {
                val timestamp = selectedDate.timeInMillis
                if (isIncome) {
                    txnRepo.addIncome(
                        projectId = projectId,
                        amountMinor = amountMinor,
                        title = title.trim(),
                        timestampEpochMs = timestamp,
                        notes = notes.trim().ifEmpty { null }
                    )
                } else {
                    txnRepo.addExpense(
                        projectId = projectId,
                        amountMinor = amountMinor,
                        title = title.trim(),
                        timestampEpochMs = timestamp,
                        notes = notes.trim().ifEmpty { null }
                    )
                }
                onDismissRequest()
            }
        }
    }

    AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text(dialogTitle, color = MaterialTheme.colorScheme.onSurface) },
            text = {
            Column {
                // Amount Field
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount", color = MaterialTheme.colorScheme.onSurface) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    trailingIcon = {
                        IconButton(onClick = { showCalculator = true }) {
                            Icon(Icons.Default.Calculate, contentDescription = "Calculator")
                        }
                    }
                )

                Spacer(modifier = Modifier.padding(12.dp))

                // Date Field with Calendar Icon
                OutlinedTextField(
                    value = dateFormat.format(selectedDate.time),
                    onValueChange = { },
                    label = { Text("Date (DD-MM-YYYY)", color = MaterialTheme.colorScheme.onSurface) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker = true },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.CalendarToday, contentDescription = "Select Date")
                        }
                    }
                )

                Spacer(modifier = Modifier.padding(12.dp))

                // Time Field with Clock Icon (Optional)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = if (useCustomTime) timeFormat.format(selectedDate.time) else "Current Time",
                        onValueChange = { },
                        label = { Text("Time (Optional)", color = MaterialTheme.colorScheme.onSurface) },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { 
                                useCustomTime = true
                                showTimePicker = true 
                            },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { 
                                useCustomTime = true
                                showTimePicker = true 
                            }) {
                                Icon(Icons.Default.Schedule, contentDescription = "Select Time")
                            }
                        }
                    )
                    
                    if (useCustomTime) {
                        IconButton(onClick = { 
                            useCustomTime = false
                            selectedDate = Calendar.getInstance()
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "Use Current Time")
                        }
                    }
                }

                Spacer(modifier = Modifier.padding(12.dp))

                // Title Field
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title", color = MaterialTheme.colorScheme.onSurface) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                )

                Spacer(modifier = Modifier.padding(12.dp))

                // Notes Field
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (Optional)", color = MaterialTheme.colorScheme.onSurface) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    minLines = 3,
                    maxLines = 5,
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { saveTransaction() },
                enabled = isValid && !isSaving,
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = buttonColor
                )
            ) {
                Text(if (isSaving) "Saving..." else "Save Transaction")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
    
    // Date Picker Dialog
    if (showDatePicker) {
        DatePickerDialog(
            onDateSelected = { calendar ->
                selectedDate = calendar
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
    
    // Time Picker Dialog
    if (showTimePicker) {
        TimePickerDialog(
            initialTime = selectedDate,
            onTimeSelected = { calendar ->
                selectedDate = calendar
                showTimePicker = false
            },
            onDismiss = { showTimePicker = false }
        )
    }
    
    // Calculator Dialog
    if (showCalculator) {
        Dialog(onDismissRequest = { showCalculator = false }) {
            CalculatorBottomSheet(
                state = calculatorState,
                onAction = { action ->
                    calculatorViewModel.onAction(action)
                },
                onConfirm = { result ->
                    amount = result
                    showCalculator = false
                }
            )
        }
    }
}

@Composable
fun DatePickerDialog(
    onDateSelected: (Calendar) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Date", color = MaterialTheme.colorScheme.onSurface) },
        text = {
            Column {
                Text(
                    text = "Selected: ${SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(selectedDate.time)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                // Simple date input fields
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = selectedDate.get(Calendar.DAY_OF_MONTH).toString(),
                        onValueChange = { day ->
                            day.toIntOrNull()?.let { d ->
                                if (d in 1..31) {
                                    selectedDate.set(Calendar.DAY_OF_MONTH, d)
                                }
                            }
                        },
                        label = { Text("Day", color = MaterialTheme.colorScheme.onSurface) },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    )
                    OutlinedTextField(
                        value = (selectedDate.get(Calendar.MONTH) + 1).toString(),
                        onValueChange = { month ->
                            month.toIntOrNull()?.let { m ->
                                if (m in 1..12) {
                                    selectedDate.set(Calendar.MONTH, m - 1)
                                }
                            }
                        },
                        label = { Text("Month", color = MaterialTheme.colorScheme.onSurface) },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    )
                    OutlinedTextField(
                        value = selectedDate.get(Calendar.YEAR).toString(),
                        onValueChange = { year ->
                            year.toIntOrNull()?.let { y ->
                                if (y > 1900) {
                                    selectedDate.set(Calendar.YEAR, y)
                                }
                            }
                        },
                        label = { Text("Year", color = MaterialTheme.colorScheme.onSurface) },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = { onDateSelected(selectedDate) }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun TimePickerDialog(
    initialTime: Calendar,
    onTimeSelected: (Calendar) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedTime by remember { mutableStateOf(initialTime) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Time", color = MaterialTheme.colorScheme.onSurface) },
        text = {
            Column {
                Text(
                    text = "Selected: ${SimpleDateFormat("HH:mm", Locale.getDefault()).format(selectedTime.time)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                // Simple time input fields
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = selectedTime.get(Calendar.HOUR_OF_DAY).toString(),
                        onValueChange = { hour ->
                            hour.toIntOrNull()?.let { h ->
                                if (h in 0..23) {
                                    selectedTime.set(Calendar.HOUR_OF_DAY, h)
                                }
                            }
                        },
                        label = { Text("Hour", color = MaterialTheme.colorScheme.onSurface) },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    )
                    OutlinedTextField(
                        value = selectedTime.get(Calendar.MINUTE).toString(),
                        onValueChange = { minute ->
                            minute.toIntOrNull()?.let { m ->
                                if (m in 0..59) {
                                    selectedTime.set(Calendar.MINUTE, m)
                                }
                            }
                        },
                        label = { Text("Minute", color = MaterialTheme.colorScheme.onSurface) },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = { onTimeSelected(selectedTime) }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}