package com.example.accountsmanagerapplication.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import android.util.Log
import com.example.accountsmanagerapplication.ui.theme.SuccessGreen
import com.example.accountsmanagerapplication.ui.theme.ErrorRed
import com.example.accountsmanagerapplication.ui.theme.LocalThemeState
import com.example.accountsmanagerapplication.util.CsvExportUtil
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.accountsmanagerapplication.data.repo.ProjectRepository
import com.example.accountsmanagerapplication.data.repo.TransactionRepository
import com.example.accountsmanagerapplication.di.AppModule
import com.example.accountsmanagerapplication.util.formatCurrencyMinor
import com.example.accountsmanagerapplication.data.dao.ProjectBalanceRow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: ProjectListViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val projectBalances by viewModel.projectBalances.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val themeStateManager = LocalThemeState.current

    // Search state
    var showSearchBar by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    // Dialog state
    var showCreateProjectDialog by remember { mutableStateOf(false) }

    // Share launcher for CSV export
    val shareLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { }

    // Filter projects based on search query
    val filteredProjects = if (searchQuery.isBlank()) {
        projectBalances
    } else {
        projectBalances.filter { project ->
            project.name.contains(searchQuery, ignoreCase = true) ||
            project.description?.contains(searchQuery, ignoreCase = true) == true
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { },
                navigationIcon = {
                    Text(
                        text = "Projects",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                },
                actions = {
                    IconButton(onClick = { showSearchBar = !showSearchBar }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = {
                        scope.launch {
                            try {
                                android.util.Log.d("ExportDebug", "Starting CSV export process")
                                val (balances, txByProject) = viewModel.prepareAllProjectsExportData()
                                android.util.Log.d("ExportDebug", "Prepared data: ${balances.size} projects, ${txByProject.size} transaction groups")

                                val csv = CsvExportUtil.generateAllProjectsCsv(balances, txByProject)
                                android.util.Log.d("ExportDebug", "Generated CSV: ${csv.length} characters")

                                // Save to cache for sharing (simpler approach)
                                val cacheDir = File(context.cacheDir, "exports").apply { mkdirs() }
                                val timestamp = java.text.SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", java.util.Locale.getDefault()).format(java.util.Date())
                                val fileName = "accounts_export_$timestamp.csv"
                                val file = File(cacheDir, fileName)
                                file.writeText(csv)
                                android.util.Log.d("ExportDebug", "File saved to: ${file.absolutePath}")

                                val uri = FileProvider.getUriForFile(context, context.packageName + ".fileprovider", file)
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/csv"
                                    putExtra(Intent.EXTRA_STREAM, uri)
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                }
                                shareLauncher.launch(Intent.createChooser(intent, "Export CSV"))
                                android.util.Log.d("ExportDebug", "Share intent launched successfully")
                            } catch (e: Exception) {
                                android.util.Log.e("ExportError", "CSV export failed", e)
                                // You might want to show a toast or snackbar to the user here
                            }
                        }
                    }) {
                        Icon(Icons.Default.Upload, contentDescription = "Export")
                    }
                    IconButton(onClick = { themeStateManager.toggleTheme() }) {
                        Icon(
                            if (themeStateManager.isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode, 
                            contentDescription = "Toggle Theme"
                        )
                    }
                    IconButton(onClick = { navController.navigate("log_viewer") }) {
                        Icon(Icons.Default.BugReport, contentDescription = "View Crash Logs")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateProjectDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "New Project")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search Bar (only visible when toggled)
            if (showSearchBar) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search projects...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                )
            }
            
            // Projects List or Empty State
            if (filteredProjects.isEmpty()) {
                // Empty State
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier.padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Text(
                            text = "No projects found.\nCreate your first project to get started!",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.padding(24.dp)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredProjects) { row ->
                        ProjectCard(row = row) {
                            navController.navigate("project_detail/${row.projectId}")
                        }
                    }
                }
            }
        }
    }

    // Create Project Dialog
    if (showCreateProjectDialog) {
        CreateProjectDialog(
            onDismissRequest = { showCreateProjectDialog = false }
        )
    }
}

@Composable
private fun ProjectCard(row: ProjectBalanceRow, onClick: () -> Unit) {
    val balanceColor: Color = if (row.balance >= 0) SuccessGreen else ErrorRed
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Project name and balance in same row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = row.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = formatCurrencyMinor(row.balance),
                    style = MaterialTheme.typography.titleLarge,
                    color = balanceColor,
                    fontWeight = FontWeight.Bold
                )
            }
            
            // Description below
            if (!row.description.isNullOrBlank()) {
                Text(
                    text = row.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            } else {
                Text(
                    text = "No description",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

// moved to util/Formatters.kt