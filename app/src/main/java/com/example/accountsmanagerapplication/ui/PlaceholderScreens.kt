package com.example.accountsmanagerapplication.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.accountsmanagerapplication.ui.theme.SuccessGreen
import com.example.accountsmanagerapplication.ui.theme.ErrorRed
import com.example.accountsmanagerapplication.ui.theme.SuccessGreenLight
import com.example.accountsmanagerapplication.ui.theme.ErrorRedLight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.accountsmanagerapplication.data.TransactionEntity
import com.example.accountsmanagerapplication.util.formatCurrencyMinor
import com.example.accountsmanagerapplication.util.CsvExportUtil
import androidx.compose.ui.platform.LocalContext
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.accountsmanagerapplication.util.ExporterService
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.runtime.rememberCoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProjectScreen(navController: NavController, vm: CreateProjectViewModel = viewModel()) {
    val name by vm.name.collectAsState()
    val description by vm.description.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Project") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
                value = name,
                onValueChange = vm::onNameChange,
                label = { Text("Project Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = description,
                onValueChange = vm::onDescriptionChange,
                label = { Text("Description (Optional)") },
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
            )
            Button(
                onClick = {
                    vm.saveProject()
                    navController.popBackStack()
                },
                enabled = name.isNotBlank(),
                modifier = Modifier
                    .padding(top = 24.dp)
            ) {
                Text("Save Project")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailScreen(navController: NavController, vm: ProjectDetailViewModel = viewModel()) {
    val project by vm.project.collectAsState()
    val transactions by vm.transactions.collectAsState()
    val totalCredit by vm.totalCredit.collectAsState()
    val totalDebit by vm.totalDebit.collectAsState()
    val balance by vm.runningBalance.collectAsState()
    
    var showAddTransactionDialog by remember { mutableStateOf(false) }
    var transactionType by remember { mutableStateOf("income") }
    var sortOrder by remember { mutableStateOf("newest") } // "newest" or "oldest"
    
    // Collapsible header state
    var isFinancialSummaryExpanded by remember { mutableStateOf(true) }
    val listState = rememberLazyListState()
    
    // Description popup state
    var showDescriptionPopup by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            val context = LocalContext.current
            val shareLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { }
            CenterAlignedTopAppBar(
                title = { },
                navigationIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                        Text(
                            text = project?.name ?: "Project",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .clickable { 
                                    if (!project?.description.isNullOrBlank()) {
                                        showDescriptionPopup = true
                                    }
                                }
                        )
                    }
                },
                actions = {
                    val context = LocalContext.current
                    val scope = rememberCoroutineScope()
                    
                    // Launcher for the notification permission
                    val notificationPermissionLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestPermission()
                    ) { isGranted: Boolean ->
                        if (isGranted) {
                            Toast.makeText(context, "Permission granted. You can now receive export notifications.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Permission denied. Notifications will not be shown.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    
                    IconButton(onClick = {
                        // The export logic
                        val projectValue = project // Get the current state value
                        val transactionsValue = transactions

                        if (projectValue != null && transactionsValue.isNotEmpty()) {
                            val exportAction = {
                                scope.launch(Dispatchers.IO) {
                                    val csvContent = CsvExportUtil.generateSingleProjectCsv(projectValue, transactionsValue)
                                    val exporterService = ExporterService(context)
                                    withContext(Dispatchers.Main) {
                                       exporterService.exportProject(projectValue.name, csvContent)
                                    }
                                }
                            }

                            // Check for notification permission on Android 13+
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                when (ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)) {
                                    PackageManager.PERMISSION_GRANTED -> {
                                        // Permission is already granted, proceed with export
                                        exportAction()
                                    }
                                    else -> {
                                        // Permission is not granted, request it
                                        notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                                        // Note: You might want to handle the case where the user grants permission
                                        // by re-triggering the export inside the launcher's callback.
                                    }
                                }
                            } else {
                                // For older versions, no special permission is needed to post notifications
                                exportAction()
                            }
                        } else {
                            Toast.makeText(context, "No data to export.", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Icon(Icons.Default.Upload, contentDescription = "Export Project")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            // Track scroll position to auto-expand when scrolling back to top
            LaunchedEffect(listState.firstVisibleItemIndex) {
                // Auto-expand when scrolling back to the very top
                if (listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0) {
                    isFinancialSummaryExpanded = true
                }
            }
            
            val alpha by animateFloatAsState(
                targetValue = if (isFinancialSummaryExpanded) 1f else 0f,
                animationSpec = tween(300),
                label = "financial_summary_alpha"
            )
            
            val height by animateFloatAsState(
                targetValue = if (isFinancialSummaryExpanded) 1f else 0f,
                animationSpec = tween(300),
                label = "financial_summary_height"
            )
            
            if (isFinancialSummaryExpanded || alpha > 0f) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .alpha(alpha),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        // Top Row: Credit and Debit - Properly divided in half
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Left Half: Total Credit
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Total Credit",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = formatCurrencyMinor(totalCredit),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = SuccessGreen,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                            
                            // Right Half: Total Debit
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Total Debit",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = formatCurrencyMinor(totalDebit),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = ErrorRed,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.padding(vertical = 8.dp))
                        
                        // Bottom Row: Balance (centered, smaller font)
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Balance",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = formatCurrencyMinor(balance),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (balance >= 0) SuccessGreen else ErrorRed
                            )
                        }
                    }
                }
            }

            // Transactions Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Transactions Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Transactions",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Collapse/Expand Arrow
                            IconButton(onClick = { 
                                isFinancialSummaryExpanded = !isFinancialSummaryExpanded
                            }) {
                                Icon(
                                    if (isFinancialSummaryExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = if (isFinancialSummaryExpanded) "Collapse" else "Expand"
                                )
                            }
                            
                            // Sort Button
                            IconButton(onClick = { 
                                sortOrder = if (sortOrder == "newest") "oldest" else "newest"
                            }) {
                                Icon(
                                    if (sortOrder == "newest") Icons.Default.ArrowDownward else Icons.Default.ArrowUpward, 
                                    contentDescription = "Sort"
                                )
                            }
                        }
                    }

                    // Action Buttons - Compact Design
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Add Expense Button - Compact
                        IconButton(
                            onClick = { 
                                transactionType = "expense"
                                showAddTransactionDialog = true 
                            },
                            modifier = Modifier.weight(1f),
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = ErrorRed
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    Icons.Default.Remove, 
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onError
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    "Expense",
                                    color = MaterialTheme.colorScheme.onError,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                        
                        // Add Income Button - Compact
                        IconButton(
                            onClick = { 
                                transactionType = "income"
                                showAddTransactionDialog = true 
                            },
                            modifier = Modifier.weight(1f),
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = SuccessGreen
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    Icons.Default.Add, 
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    "Income",
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                    }

                    // Transaction Table Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Title",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(2f)
                        )
                        Text(
                            text = "Credit/Debit",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }

                    // Transaction List
                    if (transactions.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No transactions yet.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    } else {
                        // Sort transactions based on sortOrder
                        val sortedTransactions = if (sortOrder == "newest") {
                            transactions.sortedByDescending { it.timestampEpochMs }
                        } else {
                            transactions.sortedBy { it.timestampEpochMs }
                        }

                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(sortedTransactions) { txn ->
                                val pid = project?.id
                                
                                TransactionRow(txn, 0L) { // runningBalance not used anymore
                                    if (pid != null) {
                                        navController.navigate("project/${pid}/edit_transaction/${txn.id}")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Add Transaction Dialog
    if (showAddTransactionDialog) {
        project?.let { currentProject ->
            AddTransactionDialog(
                onDismissRequest = { showAddTransactionDialog = false },
                transactionType = transactionType,
                projectId = currentProject.id
            )
        }
    }

    // Description Popup
    if (showDescriptionPopup && !project?.description.isNullOrBlank()) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showDescriptionPopup = false },
            title = { 
                Text(
                    text = project?.name ?: "Project",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = project?.description ?: "",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                androidx.compose.material3.TextButton(
                    onClick = { showDescriptionPopup = false }
                ) {
                    Text("Close")
                }
            }
        )
    }

}

@Composable
private fun HeaderStats(totalCredit: Long, totalDebit: Long, balance: Long) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        StatCard(
            title = "Total Credit", 
            value = formatCurrencyMinor(totalCredit), 
            backgroundColor = SuccessGreenLight,
            textColor = SuccessGreen,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            title = "Total Debit", 
            value = formatCurrencyMinor(totalDebit), 
            backgroundColor = ErrorRedLight,
            textColor = ErrorRed,
            modifier = Modifier.weight(1f)
        )
        val balColor = if (balance >= 0) SuccessGreen else ErrorRed
        val balBackgroundColor = if (balance >= 0) SuccessGreenLight else ErrorRedLight
        StatCard(
            title = "Balance", 
            value = formatCurrencyMinor(balance), 
            backgroundColor = balBackgroundColor,
            textColor = balColor,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatCard(
    title: String, 
    value: String, 
    backgroundColor: Color, 
    textColor: Color, 
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun TransactionRow(txn: TransactionEntity, runningBalance: Long, onClick: () -> Unit) {
    val isCredit = txn.creditAmount > 0
    val amount = if (isCredit) txn.creditAmount else txn.debitAmount
    val backgroundColor = if (isCredit) SuccessGreen.copy(alpha = 0.1f) else ErrorRed.copy(alpha = 0.1f)
    val borderColor = if (isCredit) SuccessGreen.copy(alpha = 0.3f) else ErrorRed.copy(alpha = 0.3f)
    val textColor = if (isCredit) SuccessGreen else ErrorRed
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        // Background with subtle color
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(0.dp)
                )
        )
        
        // Left border for color indication
        Box(
            modifier = Modifier
                .width(4.dp)
                .fillMaxSize()
                .background(
                    color = borderColor,
                    shape = RoundedCornerShape(0.dp)
                )
        )
        
        Row(
            modifier = Modifier
        .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Title Column (Date + Description)
            Column(
                modifier = Modifier.weight(2f)
            ) {
                Text(
                    text = java.text.SimpleDateFormat("dd/MM/yy", java.util.Locale.getDefault()).format(java.util.Date(txn.timestampEpochMs)),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = txn.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            // Amount Column - More centered
            Text(
                text = formatCurrencyMinor(amount),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = textColor,
                modifier = Modifier.weight(1f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}


