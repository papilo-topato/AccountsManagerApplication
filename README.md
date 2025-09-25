# Accounts Manager Application

[![Version](https://img.shields.io/badge/version-1.0-blue.svg)](https://github.com/papilo-topato/AccountsManagerApplication)
[![Android](https://img.shields.io/badge/platform-Android-green.svg)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/language-Kotlin-purple.svg)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-orange.svg)](https://developer.android.com/jetpack/compose)

A comprehensive Android application for managing personal and business accounts with advanced transaction tracking, financial analytics, and data export capabilities.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technical Architecture](#technical-architecture)
- [Installation](#installation)
- [Usage](#usage)
- [API Documentation](#api-documentation)
- [Database Schema](#database-schema)
- [Development](#development)
- [Contributing](#contributing)
- [License](#license)

## 🎯 Overview

**Developed by:** Raghram K S

The Accounts Manager Application is a sophisticated financial management tool built with modern Android development practices. It provides users with comprehensive tools to track income, expenses, and financial analytics across multiple projects with advanced search, filtering, and export capabilities.

### Key Highlights

- **Modern Architecture**: Built with MVVM pattern using Jetpack Compose
- **Advanced Search**: Real-time transaction search with multiple filter options
- **Data Export**: CSV export functionality with customizable formats
- **Indian Numbering System**: Native support for Indian currency formatting
- **Offline First**: Complete offline functionality with Room database
- **Material Design 3**: Modern, accessible UI following Material Design guidelines

## ✨ Features

### 🏢 Project Management
- **Multi-Project Support**: Create and manage multiple financial projects
- **Project Analytics**: Comprehensive financial summaries per project
- **Project Export**: Individual project data export capabilities
- **Project Trash**: Soft delete with recovery options

### 💰 Transaction Management
- **CRUD Operations**: Full Create, Read, Update, Delete functionality
- **Transaction Types**: Support for Income (Credit) and Expense (Debit) transactions
- **Date Management**: Editable transaction dates with dd/MM/yyyy format
- **Transaction Search**: Real-time search by title, amount, or notes
- **Advanced Filtering**: Filter by transaction type, date range, and amount
- **Sorting Options**: Sort by date (newest/oldest) and amount

### 📊 Financial Analytics
- **Real-time Balance**: Live calculation of project balances
- **Credit/Debit Totals**: Separate tracking of income and expenses
- **Running Balance**: Historical balance tracking per transaction
- **Financial Summary**: Collapsible financial overview cards
- **Indian Number Formatting**: Native support for Indian numbering system (e.g., 34,56,789rs)

### 🔍 Search & Filter
- **Real-time Search**: Instant search across transaction titles, amounts, and notes
- **Filter Types**: Search by title or amount with dedicated filter chips
- **Search History**: Persistent search state across app sessions
- **Empty State Handling**: User-friendly messages for no results

### 📤 Data Export
- **CSV Export**: Export project data to CSV format
- **Multiple Export Types**: All projects or individual project exports
- **Custom Formatting**: Date-only format (no time components)
- **File Management**: Automatic file naming and organization
- **Share Integration**: Native Android sharing capabilities

### 🎨 User Interface
- **Material Design 3**: Modern, accessible UI components
- **Dark/Light Theme**: Automatic theme switching support
- **Responsive Design**: Optimized for various screen sizes
- **Accessibility**: Full accessibility support with content descriptions
- **Smooth Animations**: Fluid transitions and micro-interactions

## 🏗️ Technical Architecture

### Architecture Pattern
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   UI Layer      │    │  Business Logic │    │   Data Layer    │
│                 │    │                 │    │                 │
│ • Compose UI    │◄──►│ • ViewModels    │◄──►│ • Repository    │
│ • Navigation    │    │ • Use Cases     │    │ • Room Database │
│ • State Mgmt    │    │ • State Flow    │    │ • DAOs          │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### Technology Stack

#### Frontend
- **UI Framework**: Jetpack Compose
- **Navigation**: Navigation Compose
- **State Management**: StateFlow, MutableStateFlow
- **Material Design**: Material 3 Components
- **Animations**: Compose Animation APIs

#### Backend
- **Database**: Room (SQLite)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Dependency Injection**: Manual DI with AppModule
- **Coroutines**: Kotlin Coroutines for async operations
- **Flow**: Reactive data streams

#### Data Management
- **Local Storage**: Room Database
- **Data Export**: CSV generation with custom formatting
- **File Management**: Android FileProvider
- **Notifications**: NotificationManager for export status

### Core Components

#### ViewModels
- **ProjectListViewModel**: Manages project list and search
- **ProjectDetailViewModel**: Handles project-specific operations and search
- **TransactionEditViewModel**: Manages transaction CRUD operations
- **CreateProjectViewModel**: Handles project creation

#### Repositories
- **ProjectRepository**: Project data operations
- **TransactionRepository**: Transaction CRUD operations
- **DeletedProjectRepository**: Trash management

#### Database Entities
- **ProjectEntity**: Project information and metadata
- **TransactionEntity**: Transaction details and financial data
- **CategoryEntity**: Transaction categorization (future feature)
- **DeletedProjectEntity**: Soft delete tracking

## 📱 Installation

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 24+ (Android 7.0)
- Kotlin 1.8.0+
- Gradle 7.0+

### Build Instructions

1. **Clone the repository**
   ```bash
   git clone https://github.com/papilo-topato/AccountsManagerApplication.git
   cd AccountsManagerApplication
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Open the project directory
   - Wait for Gradle sync to complete

3. **Build the project**
   ```bash
   ./gradlew assembleDebug
   ```

4. **Install on device**
   ```bash
   ./gradlew installDebug
   ```

### APK Download
- **Debug APK**: Available in `app/build/outputs/apk/debug/`
- **Release APK**: Build with `./gradlew assembleRelease`

## 🚀 Usage

### Getting Started

1. **Launch the Application**
   - Open the app from your device's app drawer
   - The app will start with the dashboard screen

2. **Create Your First Project**
   - Tap the "+" button to create a new project
   - Enter project name and optional description
   - Tap "Save Project"

3. **Add Transactions**
   - Open a project to view its details
   - Tap "Income" or "Expense" buttons
   - Fill in transaction details (amount, title, date, notes)
   - Tap "Save Transaction"

### Advanced Features

#### Search and Filter
1. **Enable Search**
   - Tap the search icon in the project detail screen
   - Choose filter type (Title or Amount)
   - Type your search query

2. **Filter Options**
   - **Title Filter**: Search transaction titles
   - **Amount Filter**: Search by transaction amounts
   - **Real-time Results**: Results update as you type

#### Data Export
1. **Export Project Data**
   - Open a project
   - Tap the export icon (upload symbol)
   - Grant notification permissions if prompted
   - Choose sharing method (email, cloud storage, etc.)

2. **Export All Projects**
   - Use the main dashboard export option
   - Select export format and destination

#### Transaction Management
1. **Edit Transactions**
   - Tap any transaction in the list
   - Modify amount, title, date, or notes
   - Tap "Save Transaction"

2. **Delete Transactions**
   - Tap a transaction to edit
   - Tap the delete icon
   - Confirm deletion

## 📚 API Documentation

### ViewModels

#### ProjectDetailViewModel
```kotlin
class ProjectDetailViewModel {
    // State flows
    val project: StateFlow<ProjectEntity?>
    val transactions: StateFlow<List<TransactionEntity>>
    val totalCredit: StateFlow<Long>
    val totalDebit: StateFlow<Long>
    val runningBalance: StateFlow<Long>
    
    // Search functionality
    val isSearchActive: StateFlow<Boolean>
    val searchQuery: StateFlow<String>
    val searchFilter: StateFlow<SearchFilter>
    val filteredTransactions: StateFlow<List<TransactionEntity>>
    
    // Control functions
    fun onToggleSearch()
    fun onSearchQueryChange(query: String)
    fun setSearchFilter(filter: SearchFilter)
}
```

#### TransactionEditViewModel
```kotlin
class TransactionEditViewModel {
    // State flows
    val amount: StateFlow<String>
    val title: StateFlow<String>
    val notes: StateFlow<String>
    val date: StateFlow<String>
    
    // Mode detection
    val isEditMode: Boolean
    val isIncome: Boolean
    
    // Control functions
    fun onAmountChange(value: String)
    fun onTitleChange(value: String)
    fun onNotesChange(value: String)
    fun onDateChange(value: String)
    fun saveTransaction()
    fun deleteTransaction()
    fun isValid(): Boolean
}
```

### Repositories

#### TransactionRepository
```kotlin
class TransactionRepository {
    // Observation methods
    fun observeTransactions(projectId: Long): Flow<List<TransactionEntity>>
    fun observeTransactionById(id: Long): Flow<TransactionEntity?>
    
    // CRUD operations
    suspend fun addIncome(projectId: Long, amountMinor: Long, title: String, 
                         timestampEpochMs: Long, notes: String?): Long
    suspend fun addExpense(projectId: Long, amountMinor: Long, title: String, 
                          timestampEpochMs: Long, notes: String?): Long
    suspend fun update(entity: TransactionEntity)
    suspend fun delete(entity: TransactionEntity)
}
```

### Database Schema

#### ProjectEntity
```kotlin
@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String?,
    val createdAt: Long = System.currentTimeMillis(),
    val displayOrder: Int = 0
)
```

#### TransactionEntity
```kotlin
@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val projectId: Long,
    val title: String,
    val notes: String?,
    val creditAmount: Long = 0,
    val debitAmount: Long = 0,
    val timestampEpochMs: Long = System.currentTimeMillis(),
    val categoryId: Long? = null
)
```

### Utility Classes

#### CsvExportUtil
```kotlin
object CsvExportUtil {
    fun generateAllProjectsCsv(
        projects: List<ProjectBalanceRow>,
        transactionsByProject: Map<Long, List<TransactionEntity>>
    ): String
    
    fun generateSingleProjectCsv(
        project: ProjectEntity,
        transactions: List<TransactionEntity>
    ): String
}
```

#### Formatters
```kotlin
object Formatters {
    fun formatCurrencyMinor(amountMinor: Long): String
    private fun formatIndianNumber(number: Long): String
}
```

## 🗄️ Database Schema

### Tables

#### Projects Table
| Column | Type | Description |
|--------|------|-------------|
| id | INTEGER PRIMARY KEY | Auto-generated project ID |
| name | TEXT NOT NULL | Project name |
| description | TEXT | Optional project description |
| created_at | INTEGER | Creation timestamp |
| display_order | INTEGER | Sort order for projects |

#### Transactions Table
| Column | Type | Description |
|--------|------|-------------|
| id | INTEGER PRIMARY KEY | Auto-generated transaction ID |
| project_id | INTEGER NOT NULL | Foreign key to projects table |
| title | TEXT NOT NULL | Transaction title |
| notes | TEXT | Optional transaction notes |
| credit_amount | INTEGER | Credit amount in minor units (paise) |
| debit_amount | INTEGER | Debit amount in minor units (paise) |
| timestamp_epoch_ms | INTEGER | Transaction timestamp |
| category_id | INTEGER | Optional category reference |

### Relationships
- **One-to-Many**: Project → Transactions
- **Foreign Key**: transactions.project_id → projects.id
- **Cascade Delete**: Deleting a project removes all its transactions

### Indexes
- **Primary Keys**: All tables have auto-incrementing primary keys
- **Foreign Keys**: project_id is indexed for efficient joins
- **Timestamps**: timestamp_epoch_ms is indexed for sorting

## 🛠️ Development

### Project Structure
```
app/
├── src/main/java/com/example/accountsmanagerapplication/
│   ├── data/                    # Data layer
│   │   ├── dao/                 # Data Access Objects
│   │   ├── repo/                # Repository implementations
│   │   └── *.kt                 # Entity classes
│   ├── di/                      # Dependency Injection
│   ├── navigation/              # Navigation components
│   ├── ui/                      # UI layer
│   │   ├── *.kt                 # Composable screens
│   │   └── theme/               # Theme and styling
│   ├── util/                    # Utility classes
│   └── MainActivity.kt          # Application entry point
├── src/main/res/                # Resources
│   ├── drawable/                # Icons and images
│   ├── layout/                  # XML layouts (if any)
│   └── values/                  # Strings, colors, themes
└── build.gradle.kts             # Module build configuration
```

### Key Dependencies
```kotlin
// Core Android
implementation("androidx.core:core-ktx:1.10.1")
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

// Compose
implementation("androidx.activity:activity-compose:1.7.2")
implementation("androidx.compose.ui:ui:1.4.3")
implementation("androidx.compose.material3:material3:1.1.1")

// Navigation
implementation("androidx.navigation:navigation-compose:2.6.0")

// Room Database
implementation("androidx.room:room-runtime:2.5.0")
implementation("androidx.room:room-ktx:2.5.0")
kapt("androidx.room:room-compiler:2.5.0")
```

### Build Configuration
- **Target SDK**: 34 (Android 14)
- **Min SDK**: 24 (Android 7.0)
- **Compile SDK**: 34
- **Kotlin Version**: 1.8.0
- **Gradle Version**: 8.0

### Code Style
- **Kotlin Coding Conventions**: Follow official Kotlin style guide
- **Compose Guidelines**: Follow Jetpack Compose best practices
- **Architecture**: MVVM with Repository pattern
- **Naming**: camelCase for variables, PascalCase for classes

## 🤝 Contributing

### Development Setup
1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Make your changes following the coding standards
4. Test your changes thoroughly
5. Commit your changes: `git commit -m 'Add amazing feature'`
6. Push to the branch: `git push origin feature/amazing-feature`
7. Open a Pull Request

### Code Review Process
- All changes require code review
- Ensure all tests pass
- Follow the established architecture patterns
- Update documentation for new features
- Maintain backward compatibility

### Reporting Issues
- Use GitHub Issues for bug reports
- Provide detailed reproduction steps
- Include device information and Android version
- Attach relevant logs if possible

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **Material Design Team** for the comprehensive design system
- **Jetpack Compose Team** for the modern UI toolkit
- **Android Room Team** for the excellent database abstraction
- **Kotlin Team** for the powerful programming language

## 📞 Support

For support, email raghramks@example.com or create an issue in the GitHub repository.

---

**Version**: 1.0  
**Last Updated**: December 2024  
**Maintainer**: Raghram K S
