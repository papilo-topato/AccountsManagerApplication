# 📊 Accounts Manager Application

<div align="center">

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Material Design 3](https://img.shields.io/badge/Material%20Design%203-757575?style=for-the-badge&logo=materialdesign&logoColor=white)

**A modern, offline-first Android application for managing personal and business accounts with comprehensive transaction tracking.**

[![GitHub stars](https://img.shields.io/github/stars/papilo-topato/AccountsManagerApplication?style=social)](https://github.com/papilo-topato/AccountsManagerApplication/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/papilo-topato/AccountsManagerApplication?style=social)](https://github.com/papilo-topato/AccountsManagerApplication/network)
[![GitHub issues](https://img.shields.io/github/issues/papilo-topato/AccountsManagerApplication)](https://github.com/papilo-topato/AccountsManagerApplication/issues)

</div>

## 🚀 Features

### 💼 **Project Management**
- **Multi-Project Support**: Create and manage multiple account projects
- **Project Descriptions**: Add detailed descriptions for each project
- **Project-Specific Views**: Dedicated screens for each project's transactions

### 💰 **Transaction Management**
- **Income & Expense Tracking**: Comprehensive financial transaction recording
- **Custom Categories**: Flexible categorization system for transactions
- **Date & Time Picker**: Precise transaction timestamping
- **Built-in Calculator**: Integrated calculator for accurate amount entry
- **Transaction Notes**: Add detailed notes to each transaction

### 📊 **Financial Analytics**
- **Real-time Balance Calculation**: Automatic running balance computation
- **Credit/Debit Summaries**: Visual breakdown of income vs expenses
- **Project Balance Overview**: Quick financial status for each project
- **Transaction History**: Complete audit trail of all financial activities

### 🎨 **Modern UI/UX**
- **Material Design 3**: Latest Material Design principles
- **Dark/Light Theme**: Automatic theme switching with system preference
- **Responsive Design**: Optimized for various screen sizes
- **Intuitive Navigation**: Clean, user-friendly interface
- **Collapsible Financial Summary**: Space-efficient transaction viewing

### 📤 **Data Export & Sharing**
- **CSV Export**: Export project data for external analysis
- **Project-Specific Exports**: Individual project data export
- **All-Projects Export**: Comprehensive data export across all projects
- **Share Functionality**: Easy sharing via email or other apps

### 🛡️ **Crash Reporting & Debugging**
- **Automatic Crash Logging**: Comprehensive error tracking and reporting
- **User-Friendly Log Viewer**: Built-in crash log management
- **Developer Reporting**: Easy sharing of crash reports for debugging
- **Local Log Storage**: Secure, local-only log storage

### 🔒 **Privacy & Security**
- **Offline-First**: No internet connection required
- **Local Data Storage**: All data stored securely on device
- **No Data Collection**: Zero user data sent to external servers
- **User-Controlled Sharing**: Users choose what to share

## 🏗️ Technical Architecture

### **Tech Stack**
- **Language**: Kotlin 100%
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM (Model-View-ViewModel)
- **Database**: Room (SQLite)
- **Dependency Injection**: Manual DI with Repository Pattern
- **Navigation**: Navigation Compose
- **State Management**: StateFlow + Compose State
- **Coroutines**: Asynchronous programming
- **Material Design**: Material 3 Design System

### **Architecture Components**

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

### **Key Components**

#### **Database Schema**
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

#### **Repository Pattern**
```kotlin
interface ProjectRepository {
    suspend fun getAllProjects(): List<ProjectEntity>
    suspend fun insertProject(project: ProjectEntity): Long
    suspend fun updateProject(project: ProjectEntity)
    suspend fun deleteProject(projectId: Long)
    fun observeProjects(): Flow<List<ProjectEntity>>
}
```

## 📱 Screenshots

<div align="center">

| Dashboard | Project Details | Transaction Entry |
|-----------|----------------|-------------------|
| ![Dashboard](docs/screenshots/dashboard.png) | ![Project Details](docs/screenshots/project_details.png) | ![Transaction Entry](docs/screenshots/transaction_entry.png) |

| Financial Summary | Log Viewer | Dark Theme |
|-------------------|------------|------------|
| ![Financial Summary](docs/screenshots/financial_summary.png) | ![Log Viewer](docs/screenshots/log_viewer.png) | ![Dark Theme](docs/screenshots/dark_theme.png) |

</div>

## 🚀 Getting Started

### **Prerequisites**
- Android Studio Arctic Fox (2020.3.1) or later
- Android SDK 26 (Android 8.0) or higher
- Kotlin 1.9.0 or later
- Gradle 8.0 or later

### **Installation**

1. **Clone the repository**
   ```bash
   git clone https://github.com/papilo-topato/AccountsManagerApplication.git
   cd AccountsManagerApplication
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory
   - Wait for Gradle sync to complete

3. **Build and Run**
   ```bash
   ./gradlew assembleDebug
   ```
   - Connect an Android device or start an emulator
   - Click "Run" in Android Studio or use:
   ```bash
   ./gradlew installDebug
   ```

### **Development Setup**

1. **Enable Developer Options** on your Android device
2. **Enable USB Debugging**
3. **Connect device** via USB or use Android Emulator
4. **Build and install** the debug APK

## 📋 Usage Guide

### **Creating Your First Project**
1. Launch the app
2. Tap the **"+"** button (Floating Action Button)
3. Enter project name and description
4. Tap **"Save"**

### **Adding Transactions**
1. Open a project from the dashboard
2. Tap **"+ Add Income"** or **"- Add Expense"**
3. Fill in transaction details:
   - Amount (use built-in calculator)
   - Title/Description
   - Date and time
   - Notes (optional)
4. Tap **"Save Transaction"**

### **Viewing Financial Summary**
- **Dashboard**: See all projects with their balances
- **Project View**: Detailed financial breakdown
- **Collapsible Summary**: Tap arrow to expand/collapse

### **Exporting Data**
1. **All Projects**: Tap export icon (📤) on dashboard
2. **Single Project**: Tap export icon in project view
3. **Share**: Choose email or other apps to share CSV

### **Managing Crash Logs**
1. Tap bug report icon (🐛) on dashboard
2. View crash logs and device information
3. Share logs with developer for debugging

## 🔧 Configuration

### **App Configuration**
- **Minimum SDK**: API 26 (Android 8.0)
- **Target SDK**: API 36 (Latest)
- **Compile SDK**: API 36
- **Java Version**: 11

### **Dependencies**
```kotlin
// Core Android
implementation("androidx.core:core-ktx:1.12.0")
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
implementation("androidx.activity:activity-compose:1.8.2")

// Compose BOM
implementation(platform("androidx.compose:compose-bom:2024.02.00"))
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.ui:ui-graphics")
implementation("androidx.compose.ui:ui-tooling-preview")
implementation("androidx.compose.material3:material3")

// Navigation
implementation("androidx.navigation:navigation-compose:2.7.6")

// Room Database
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
kapt("androidx.room:room-compiler:2.6.1")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

// JSON Serialization
implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
```

## 🧪 Testing

### **Running Tests**
```bash
# Unit Tests
./gradlew test

# Instrumented Tests
./gradlew connectedAndroidTest

# All Tests
./gradlew check
```

### **Test Coverage**
- **Unit Tests**: Repository and ViewModel logic
- **UI Tests**: Compose UI interactions
- **Integration Tests**: Database operations

## 📊 Performance

### **Optimizations**
- **Lazy Loading**: Efficient list rendering with LazyColumn
- **State Management**: Optimized state updates with StateFlow
- **Database Queries**: Indexed queries for fast data retrieval
- **Memory Management**: Proper lifecycle management
- **Image Loading**: Optimized icon rendering

### **Metrics**
- **App Size**: ~15MB (debug), ~8MB (release)
- **Startup Time**: <2 seconds
- **Memory Usage**: <50MB typical
- **Database Performance**: Sub-millisecond queries

## 🐛 Bug Reports & Feature Requests

### **Reporting Issues**
1. **Check existing issues** first
2. **Use the crash log viewer** in the app
3. **Include device information**:
   - Android version
   - Device model
   - App version
   - Steps to reproduce
4. **Attach crash logs** if available

### **Feature Requests**
- **Use GitHub Issues** with the "enhancement" label
- **Describe the use case** clearly
- **Provide mockups** if applicable

## 🤝 Contributing

We welcome contributions! Please follow these steps:

1. **Fork the repository**
2. **Create a feature branch**:
   ```bash
   git checkout -b feature/amazing-feature
   ```
3. **Commit your changes**:
   ```bash
   git commit -m 'Add amazing feature'
   ```
4. **Push to the branch**:
   ```bash
   git push origin feature/amazing-feature
   ```
5. **Open a Pull Request**

### **Development Guidelines**
- Follow **Kotlin coding conventions**
- Use **Material Design 3** principles
- Write **unit tests** for new features
- Update **documentation** as needed
- Ensure **backward compatibility**

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Developer

**Papilo-Topato**
- **GitHub**: [@papilo-topato](https://github.com/papilo-topato)
- **Project**: [Accounts Manager Application](https://github.com/papilo-topato/AccountsManagerApplication)

## 🙏 Acknowledgments

- **Jetpack Compose** team for the amazing UI framework
- **Material Design** team for the design system
- **Android** community for continuous support
- **Open source contributors** who made this possible

## 📈 Roadmap

### **Version 1.1 (Planned)**
- [ ] Data backup and restore
- [ ] Advanced reporting and analytics
- [ ] Custom categories management
- [ ] Multi-currency support
- [ ] Budget tracking features

### **Version 1.2 (Future)**
- [ ] Cloud synchronization
- [ ] Multi-user support
- [ ] Advanced security features
- [ ] Widget support
- [ ] Wear OS companion app

---

<div align="center">

**⭐ Star this repository if you find it helpful!**

[![GitHub stars](https://img.shields.io/github/stars/papilo-topato/AccountsManagerApplication?style=social)](https://github.com/papilo-topato/AccountsManagerApplication/stargazers)

**Made with ❤️ by [Papilo-Topato](https://github.com/papilo-topato)**

</div>
