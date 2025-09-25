package com.example.accountsmanagerapplication.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.input.pointer.pointerInput
import com.example.accountsmanagerapplication.ui.theme.SuccessGreen
import com.example.accountsmanagerapplication.ui.theme.ErrorRed
import com.example.accountsmanagerapplication.ui.theme.LocalThemeState
import androidx.navigation.NavController
import com.example.accountsmanagerapplication.util.formatCurrencyMinor
import com.example.accountsmanagerapplication.data.dao.ProjectBalanceRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: ProjectListViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val projectBalances by viewModel.projectBalances.collectAsState()
    val projects by viewModel.projects.collectAsState()
    val themeStateManager = LocalThemeState.current

    // Search state
    var showSearchBar by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    // Dialog state
    var showCreateProjectDialog by remember { mutableStateOf(false) }
    var showEditProjectDialog by remember { mutableStateOf(false) }
    var showDeleteProjectDialog by remember { mutableStateOf(false) }
    var showDescriptionDialog by remember { mutableStateOf(false) }
    var selectedProject by remember { mutableStateOf<ProjectBalanceRow?>(null) }
    
    // Multi-selection state
    var selectedProjects by remember { mutableStateOf<Set<Long>>(emptySet()) }
    var isMultiSelectionMode by remember { mutableStateOf(false) }
    
    // Dropdown menu state
    var expandedMenuProjectId by remember { mutableStateOf<Long?>(null) }
    val listState = rememberLazyListState()
    
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
                    IconButton(onClick = { themeStateManager.toggleTheme() }) {
                        Icon(
                            if (themeStateManager.isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode, 
                            contentDescription = "Toggle Theme"
                        )
                    }
                    IconButton(onClick = { navController.navigate("trash") }) {
                        Icon(Icons.Default.DeleteSweep, contentDescription = "View Trash")
                    }
                }
            )
        },
        floatingActionButton = {
            if (!isMultiSelectionMode) {
                FloatingActionButton(
                    onClick = {
                        showCreateProjectDialog = true
                    },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "New Project"
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Mode indicators
            if (isMultiSelectionMode) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Select Projects (${selectedProjects.size} selected)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Tap to exit",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { 
                            isMultiSelectionMode = false
                            selectedProjects = emptySet()
                        }
                    )
                }
            }
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
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = filteredProjects,
                        key = { project -> project.projectId }
                    ) { project ->
                        Box {
                            ProjectCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .pointerInput(project.projectId) {
                                        detectTapGestures(
                                            onLongPress = {
                                                if (isMultiSelectionMode) {
                                                    // In multi-selection mode, toggle selection
                                                    selectedProjects = if (selectedProjects.contains(project.projectId)) {
                                                        selectedProjects - project.projectId
                                                    } else {
                                                        selectedProjects + project.projectId
                                                    }
                                                } else {
                                                    // Show context menu for single project
                                                    expandedMenuProjectId = project.projectId
                                                }
                                            },
                                            onTap = {
                                                if (isMultiSelectionMode) {
                                                    // In multi-selection mode, toggle selection
                                                    selectedProjects = if (selectedProjects.contains(project.projectId)) {
                                                        selectedProjects - project.projectId
                                                    } else {
                                                        selectedProjects + project.projectId
                                                    }
                                                } else {
                                                    // A normal tap should close any open menu and then navigate
                                                    expandedMenuProjectId = null
                                                    navController.navigate("project_detail/${project.projectId}")
                                                }
                                            }
                                        )
                                    },
                                row = project,
                                isSelected = if (isMultiSelectionMode) {
                                    selectedProjects.contains(project.projectId)
                                } else {
                                    selectedProject?.projectId == project.projectId
                                },
                                isMultiSelectionMode = isMultiSelectionMode,
                                isHighlighted = (expandedMenuProjectId == project.projectId)
                            )
                            
                            // DropdownMenu anchored to this specific project card
                            DropdownMenu(
                                expanded = (expandedMenuProjectId == project.projectId),
                                onDismissRequest = { expandedMenuProjectId = null }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Edit") },
                                    onClick = {
                                        selectedProject = project
                                        showEditProjectDialog = true
                                        expandedMenuProjectId = null
                                    },
                                    leadingIcon = {
                                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Delete") },
                                    onClick = {
                                        selectedProject = project
                                        showDeleteProjectDialog = true
                                        expandedMenuProjectId = null
                                    },
                                    leadingIcon = {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                                    }
                                )
                            }
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

    // Edit Project Dialog
    selectedProject?.let { projectBalance ->
        val projectEntity = projects.find { it.id == projectBalance.projectId }
        projectEntity?.let { project ->
            if (showEditProjectDialog) {
                EditProjectDialog(
                    project = project,
                    onDismissRequest = { 
                        showEditProjectDialog = false
                        selectedProject = null
                    },
                    onProjectUpdated = { /* Project will be updated automatically via StateFlow */ }
                )
            }
        }
    }

    // Delete Project Dialog
    selectedProject?.let { projectBalance ->
        val projectEntity = projects.find { it.id == projectBalance.projectId }
        projectEntity?.let { project ->
            if (showDeleteProjectDialog) {
                DeleteProjectDialog(
                    project = project,
                    onDismissRequest = { 
                        showDeleteProjectDialog = false
                        selectedProject = null
                    },
                onConfirmDelete = {
                    // Move project to trash instead of permanent deletion
                    selectedProject?.let { projectBalance ->
                        val projectEntity = projects.find { it.id == projectBalance.projectId }
                        projectEntity?.let { project ->
                            viewModel.moveProjectToTrash(project)
                        }
                    }
                    showDeleteProjectDialog = false
                    selectedProject = null
                }
                )
            }
        }
    }

    // Long press menu - positioned relative to the selected project card
    selectedProject?.let { project ->
        DropdownMenu(
            expanded = !isMultiSelectionMode,
            onDismissRequest = { selectedProject = null }
        ) {
            DropdownMenuItem(
                text = { Text("Edit") },
                onClick = {
                    showEditProjectDialog = true
                },
                leadingIcon = {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
            )
            DropdownMenuItem(
                text = { Text("Delete") },
                onClick = {
                    showDeleteProjectDialog = true
                },
                leadingIcon = {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            )
    }
    
    // Multi-selection context menu
    if (isMultiSelectionMode && selectedProjects.isNotEmpty()) {
        DropdownMenu(
            expanded = true,
            onDismissRequest = { /* Don't dismiss on click outside */ }
        ) {
        DropdownMenuItem(
            text = { Text("Delete Selected (${selectedProjects.size})") },
            onClick = {
                showDeleteProjectDialog = true
            },
            leadingIcon = {
                Icon(Icons.Default.Delete, contentDescription = "Delete Selected")
            }
        )
        }
    }

    // Description Dialog
    selectedProject?.let { project ->
        if (showDescriptionDialog && !project.description.isNullOrBlank()) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { 
                    showDescriptionDialog = false
                    selectedProject = null
                },
                title = { 
                    Text(
                        text = project.name,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = project.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showDescriptionDialog = false
                            selectedProject = null
                            navController.navigate("project_detail/${project.projectId}")
                        }
                    ) {
                        Text("View Project")
                    }
                },
                dismissButton = {
                    androidx.compose.material3.TextButton(onClick = { 
                        showDescriptionDialog = false
                        selectedProject = null
                    }) {
                        Text("Close")
                    }
                }
            )
        }
    }
}

}

@Composable
private fun ProjectCard(
    modifier: Modifier = Modifier,
    row: ProjectBalanceRow, 
    isSelected: Boolean = false,
    isMultiSelectionMode: Boolean = false,
    isHighlighted: Boolean = false
) {
    val balanceColor: Color = if (row.balance >= 0) SuccessGreen else ErrorRed
    
    Card(
        modifier = modifier,
        border = if (isHighlighted) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
        colors = CardDefaults.cardColors(
            containerColor = when {
                isSelected && isMultiSelectionMode -> MaterialTheme.colorScheme.secondaryContainer
                isSelected -> MaterialTheme.colorScheme.primaryContainer 
                isHighlighted -> MaterialTheme.colorScheme.primaryContainer
                else -> MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = when {
                isSelected -> 8.dp
                else -> 4.dp
            }
        )
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (isMultiSelectionMode && isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Text(
                        text = row.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = formatCurrencyMinor(row.balance),
                        style = MaterialTheme.typography.titleLarge,
                        color = balanceColor,
                        fontWeight = FontWeight.Bold
                    )
                    
                }
            }
            
            // Description below (only show if not empty)
            if (!row.description.isNullOrBlank()) {
                Text(
                    text = row.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

// moved to util/Formatters.kt