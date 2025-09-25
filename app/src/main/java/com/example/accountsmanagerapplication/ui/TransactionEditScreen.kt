package com.example.accountsmanagerapplication.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionEditScreen(navController: NavController, vm: TransactionEditViewModel = viewModel()) {
    val amount by vm.amount.collectAsState()
    val title by vm.title.collectAsState()
    val notes by vm.notes.collectAsState()
    val date by vm.date.collectAsState()
    val topTitle = when {
        vm.isEditMode -> "Edit Transaction"
        vm.isIncome -> "Add Income"
        else -> "Add Expense"
    }
    var showConfirm by remember { mutableStateOf(false) }
    var shouldNavigateBack by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(topTitle) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (vm.isEditMode) {
                        IconButton(onClick = { showConfirm = true }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Delete")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = amount,
                onValueChange = vm::onAmountChange,
                label = { Text("Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = title,
                onValueChange = { newValue ->
                    android.util.Log.d("TransactionEditScreen", "Title field changed to: $newValue")
                    vm.onTitleChange(newValue)
                },
                label = { Text("Title") },
                enabled = true,
                singleLine = true,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
            )
            OutlinedTextField(
                value = date,
                onValueChange = vm::onDateChange,
                label = { Text("Date (dd/MM/yyyy)") },
                enabled = true,
                singleLine = true,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
            )
            OutlinedTextField(
                value = notes,
                onValueChange = vm::onNotesChange,
                label = { Text("Notes (Optional)") },
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
            )
            Button(
                onClick = {
                    vm.saveTransaction()
                    shouldNavigateBack = true
                },
                enabled = vm.isValid(),
                modifier = Modifier
                    .padding(top = 24.dp)
            ) {
                Text("Save Transaction")
            }
        }
    }

    if (showConfirm) {
        AlertDialog(
            onDismissRequest = { showConfirm = false },
            title = { Text("Delete Transaction") },
            text = { Text("Are you sure you want to delete this transaction?") },
            confirmButton = {
                TextButton(onClick = {
                    vm.deleteTransaction()
                    showConfirm = false
                    navController.popBackStack()
                }) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { showConfirm = false }) { Text("Cancel") }
            }
        )
    }
    
    // Handle navigation after save
    LaunchedEffect(shouldNavigateBack) {
        if (shouldNavigateBack) {
            kotlinx.coroutines.delay(200) // Give time for save to complete
            navController.popBackStack()
        }
    }
}


