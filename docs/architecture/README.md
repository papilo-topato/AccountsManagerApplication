# Architecture Documentation

This document provides detailed information about the Accounts Manager Application's architecture and design decisions.

## Overview

The Accounts Manager Application follows modern Android development best practices with a clean architecture approach, using Jetpack Compose for UI and Room for local data storage.

## Architecture Pattern

### MVVM (Model-View-ViewModel)

```
┌─────────────────────────────────────────────────────────────┐
│                    Presentation Layer                       │
├─────────────────────────────────────────────────────────────┤
│  • Jetpack Compose UI                                       │
│  • ViewModels (MVVM)                                       │
│  • Navigation Compose                                      │
│  • Material Design 3                                       │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                    Business Logic Layer                     │
├─────────────────────────────────────────────────────────────┤
│  • Repository Pattern                                      │
│  • Use Cases                                              │
│  • State Management (StateFlow)                           │
│  • Coroutines                                             │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      Data Layer                            │
├─────────────────────────────────────────────────────────────┤
│  • Room Database (SQLite)                                 │
│  • Entity Models                                          │
│  • DAO (Data Access Objects)                             │
│  • Local File Storage                                     │
└─────────────────────────────────────────────────────────────┘
```

## Component Details

### Presentation Layer

#### UI Components
- **Jetpack Compose**: Modern declarative UI framework
- **Material Design 3**: Latest Material Design system
- **Navigation Compose**: Type-safe navigation
- **State Management**: Compose State and StateFlow

#### Key Composables
```kotlin
// Dashboard Screen
@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = viewModel()
)

// Project Detail Screen
@Composable
fun ProjectDetailScreen(
    navController: NavController,
    viewModel: ProjectDetailViewModel = viewModel()
)
```

### Business Logic Layer

#### ViewModels
- **DashboardViewModel**: Manages project list and search
- **ProjectDetailViewModel**: Handles project-specific operations
- **TransactionViewModel**: Manages transaction operations

#### Repository Pattern
```kotlin
interface ProjectRepository {
    suspend fun getAllProjects(): List<ProjectEntity>
    suspend fun insertProject(project: ProjectEntity): Long
    suspend fun updateProject(project: ProjectEntity)
    suspend fun deleteProject(projectId: Long)
    fun observeProjects(): Flow<List<ProjectEntity>>
}
```

### Data Layer

#### Database Schema
```kotlin
// Project Entity
@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String?,
    val createdAt: Long = System.currentTimeMillis()
)

// Transaction Entity
@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val projectId: Long,
    val title: String,
    val notes: String?,
    val creditAmount: Long = 0,
    val debitAmount: Long = 0,
    val timestampEpochMs: Long = System.currentTimeMillis()
)
```

#### DAO (Data Access Objects)
```kotlin
@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects ORDER BY createdAt DESC")
    fun observeAllProjects(): Flow<List<ProjectEntity>>
    
    @Insert
    suspend fun insertProject(project: ProjectEntity): Long
    
    @Update
    suspend fun updateProject(project: ProjectEntity)
    
    @Delete
    suspend fun deleteProject(project: ProjectEntity)
}
```

## State Management

### Compose State
```kotlin
@Composable
fun ExampleScreen() {
    var text by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    
    // UI implementation
}
```

### ViewModel State
```kotlin
class ExampleViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ExampleUiState())
    val uiState: StateFlow<ExampleUiState> = _uiState.asStateFlow()
    
    fun updateState(newState: ExampleUiState) {
        _uiState.value = newState
    }
}
```

## Navigation

### Navigation Graph
```kotlin
@Composable
fun AppNavHost(startDestination: String = Destinations.Dashboard.route) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Destinations.Dashboard.route) {
            DashboardScreen(navController = navController)
        }
        composable(Destinations.ProjectDetail.route) {
            ProjectDetailScreen(navController = navController)
        }
        // Additional routes...
    }
}
```

## Dependency Injection

### Manual DI Pattern
```kotlin
object AppModule {
    fun provideProjectRepository(context: Context): ProjectRepository {
        val database = AppDatabase.getInstance(context)
        return ProjectRepositoryImpl(database.projectDao())
    }
    
    fun provideTransactionRepository(context: Context): TransactionRepository {
        val database = AppDatabase.getInstance(context)
        return TransactionRepositoryImpl(database.transactionDao())
    }
}
```

## Error Handling

### Crash Logging
```kotlin
object CrashLogger {
    fun logCrash(context: Context, throwable: Throwable, additionalInfo: String = "") {
        // Log crash details to local storage
        // Include device information and stack trace
    }
}
```

### Exception Handling
```kotlin
class ExampleViewModel : ViewModel() {
    fun performOperation() {
        viewModelScope.launch {
            try {
                // Perform operation
            } catch (e: Exception) {
                CrashLogger.logError(context, "Operation failed", e)
                // Handle error state
            }
        }
    }
}
```

## Performance Considerations

### Database Optimization
- **Indexing**: Proper database indexes for fast queries
- **Pagination**: Lazy loading for large datasets
- **Background Threads**: All database operations on IO dispatcher

### UI Performance
- **Lazy Loading**: LazyColumn for efficient list rendering
- **State Optimization**: Minimal state updates
- **Composition Optimization**: Proper use of remember and derivedStateOf

### Memory Management
- **Lifecycle Awareness**: Proper ViewModel lifecycle management
- **Resource Cleanup**: Dispose of resources when not needed
- **Image Optimization**: Efficient image loading and caching

## Security

### Data Protection
- **Local Storage**: All data stored locally on device
- **No Network**: Offline-first architecture
- **User Control**: Users control what data to share

### Privacy
- **No Analytics**: No user behavior tracking
- **No Data Collection**: No personal data sent to external servers
- **Transparent**: Open source code for transparency

## Testing Strategy

### Unit Tests
- **ViewModel Tests**: Test business logic
- **Repository Tests**: Test data operations
- **Utility Tests**: Test helper functions

### Integration Tests
- **Database Tests**: Test Room database operations
- **Navigation Tests**: Test navigation flows
- **End-to-End Tests**: Test complete user journeys

### UI Tests
- **Compose Tests**: Test UI components
- **Accessibility Tests**: Test accessibility features
- **Theme Tests**: Test light/dark theme switching

## Future Considerations

### Scalability
- **Modular Architecture**: Easy to add new features
- **Clean Separation**: Clear boundaries between layers
- **Extensible Design**: Easy to extend functionality

### Maintainability
- **Clear Naming**: Descriptive class and function names
- **Documentation**: Comprehensive code documentation
- **Code Standards**: Consistent coding style

### Performance
- **Monitoring**: Built-in crash logging and performance monitoring
- **Optimization**: Continuous performance improvements
- **User Feedback**: User-driven performance improvements
