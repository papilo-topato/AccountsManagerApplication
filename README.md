# 📊 Accounts Manager Application v1.0

<div align="center">

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Material Design 3](https://img.shields.io/badge/Material%20Design%203-757575?style=for-the-badge&logo=materialdesign&logoColor=white)
![Version](https://img.shields.io/badge/Version-1.0-green?style=for-the-badge)

**A modern, offline-first Android application for managing personal and business accounts with comprehensive transaction tracking.**

**Developed by Raghram K S**

[![GitHub stars](https://img.shields.io/github/stars/papilo-topato/AccountsManagerApplication?style=social)](https://github.com/papilo-topato/AccountsManagerApplication/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/papilo-topato/AccountsManagerApplication?style=social)](https://github.com/papilo-topato/AccountsManagerApplication/network)
[![GitHub issues](https://img.shields.io/github/issues/papilo-topato/AccountsManagerApplication)](https://github.com/papilo-topato/AccountsManagerApplication/issues)

</div>

## 🚀 Features

### 💼 **Project Management**
- **Multi-Project Support**: Create and manage multiple account projects
- **Project Descriptions**: Add detailed descriptions for each project
- **Project-Specific Views**: Dedicated screens for each project's transactions
- **Project Trash System**: Move projects to trash with data cleanup

### 💰 **Transaction Management**
- **Income & Expense Tracking**: Comprehensive financial transaction recording
- **Editable Transactions**: Full CRUD operations for transaction management
- **Date Selection**: Simple date picker (dd/MM/yyyy format)
- **Built-in Calculator**: Integrated calculator for accurate amount entry
- **Transaction Notes**: Add detailed notes to each transaction
- **Indian Number Formatting**: Amounts displayed in Indian numbering system (₹34,56,789.00)
- **Transaction Search**: Real-time search by title or amount
- **Transaction Sorting**: Sort by newest/oldest transactions

### 📊 **Financial Analytics**
- **Real-time Balance Calculation**: Automatic running balance updates
- **Credit/Debit Summaries**: Visual indicators with color coding
- **Project Balance Overview**: Dashboard showing all project balances
- **Collapsible Financial Summary**: Space-efficient financial overview
- **Transaction History**: Complete transaction list with running balances

### 🎨 **Modern UI/UX**
- **Material Design 3**: Latest Material Design implementation
- **Dark/Light Theme**: Automatic theme switching with manual override
- **Responsive Design**: Optimized for various screen sizes
- **Jetpack Compose**: Modern declarative UI framework
- **Intuitive Navigation**: Smooth navigation between screens
- **Search Functionality**: Advanced search with filter options

### 📤 **Data Export & Sharing**
- **CSV Export**: Export all projects or individual project data
- **Date-Only Format**: Clean CSV exports without time columns
- **Share Functionality**: Share exported files via email and other apps
- **Notification Support**: Export completion notifications
- **Comprehensive Data**: Complete transaction history with balances

### 🛠️ **Developer Features**
- **Crash Reporting**: Built-in crash logging and error tracking
- **Log Viewer**: User-accessible log viewer for debugging
- **Debug Information**: Comprehensive debugging tools
- **Performance Monitoring**: Built-in performance tracking

### 🔒 **Privacy & Security**
- **Offline-First**: No internet connection required
- **Local Storage**: All data stored locally using Room database
- **No Data Collection**: Zero external data sharing
- **User Control**: Complete control over data export and sharing

## 📱 Screenshots

<div align="center">

| Dashboard | Project Detail | Transaction Edit |
|-----------|----------------|------------------|
| ![Dashboard](docs/screenshots/dashboard.png) | ![Project Detail](docs/screenshots/project_detail.png) | ![Transaction Edit](docs/screenshots/transaction_edit.png) |

| Search & Filter | Export | Settings |
|-----------------|--------|----------|
| ![Search](docs/screenshots/search.png) | ![Export](docs/screenshots/export.png) | ![Settings](docs/screenshots/settings.png) |

</div>

## 🛠️ Technical Stack

### **Architecture**
- **MVVM Pattern**: Model-View-ViewModel architecture
- **Repository Pattern**: Clean data access layer
- **Dependency Injection**: Manual DI with AppModule
- **State Management**: StateFlow and Compose State

### **UI Framework**
- **Jetpack Compose**: Modern declarative UI
- **Material Design 3**: Latest design system
- **Navigation Compose**: Type-safe navigation
- **Compose State**: Reactive state management

### **Database**
- **Room Database**: Local SQLite database
- **Entity Relationships**: Proper foreign key relationships
- **Data Migration**: Automatic schema migrations
- **Type Converters**: Custom data type handling

### **Development Tools**
- **Kotlin**: 100% Kotlin codebase
- **Gradle**: Modern build system
- **Android Studio**: Official IDE support
- **Git**: Version control

## 📦 Installation

### **APK Download**
Download the latest APK from the [Releases](https://github.com/papilo-topato/AccountsManagerApplication/releases) page.

**APK Path**: `C:\Users\raghu\OneDrive\Desktop\Accounts - manager.apk`

### **Installation Steps**
1. **Enable Unknown Sources**:
   - Go to Settings → Security → Unknown Sources
   - Enable installation from unknown sources

2. **Download APK**:
   - Download the APK file from the releases page
   - Or use the provided APK path

3. **Install Application**:
   - Tap on the downloaded APK file
   - Follow the installation prompts
   - Grant necessary permissions

4. **Launch App**:
   - Find "Accounts Manager" in your app drawer
   - Tap to launch the application

### **System Requirements**
- **Android Version**: 8.0 (API 26) or higher
- **Storage**: 50MB free space
- **RAM**: 2GB recommended
- **Permissions**: Storage access for exports

## 🚀 Getting Started

### **First Launch**
1. **Create Your First Project**:
   - Tap the "+" button on the dashboard
   - Enter project name and description
   - Tap "Save Project"

2. **Add Transactions**:
   - Open your project
   - Tap "Income" or "Expense" buttons
   - Fill in transaction details
   - Save the transaction

3. **Explore Features**:
   - Use the search functionality
   - Export your data
   - Customize settings

### **Basic Usage**
1. **Project Management**:
   - Create multiple projects for different accounts
   - Add descriptions for better organization
   - Use the trash system for cleanup

2. **Transaction Recording**:
   - Record income and expenses
   - Use the built-in calculator
   - Add notes for context
   - Edit transactions as needed

3. **Data Analysis**:
   - View real-time balances
   - Check credit/debit summaries
   - Use search to find specific transactions

4. **Data Export**:
   - Export individual projects
   - Export all projects at once
   - Share data via email or other apps

## 🔧 Development

### **Prerequisites**
- Android Studio Arctic Fox (2020.3.1) or later
- Android SDK 26 (Android 8.0) or higher
- Kotlin 1.9.0 or later
- Git

### **Setup**
1. **Clone Repository**:
   ```bash
   git clone https://github.com/papilo-topato/AccountsManagerApplication.git
   cd AccountsManagerApplication
   ```

2. **Open in Android Studio**:
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory
   - Wait for Gradle sync to complete

3. **Build and Run**:
   ```bash
   ./gradlew assembleDebug
   ```

### **Project Structure**
```
app/
├── src/main/java/com/example/accountsmanagerapplication/
│   ├── data/                    # Data layer
│   │   ├── dao/                 # Data Access Objects
│   │   ├── repo/                # Repository implementations
│   │   └── *.kt                 # Entity classes
│   ├── di/                      # Dependency injection
│   ├── navigation/              # Navigation setup
│   ├── ui/                      # UI layer
│   │   ├── *.kt                 # Composable screens
│   │   └── theme/               # Theme configuration
│   ├── util/                    # Utility classes
│   └── MainActivity.kt          # Main activity
├── src/main/res/                # Resources
└── build.gradle.kts            # Build configuration
```

### **Key Components**
- **MainActivity**: Application entry point
- **NavGraph**: Navigation configuration
- **ViewModels**: Business logic and state management
- **Repositories**: Data access abstraction
- **DAOs**: Database access objects
- **Entities**: Data models

## 📊 Database Schema

### **Projects Table**
```sql
CREATE TABLE projects (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    description TEXT,
    created_at_epoch_ms INTEGER NOT NULL,
    display_order INTEGER DEFAULT 0
);
```

### **Transactions Table**
```sql
CREATE TABLE transactions (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    project_id INTEGER NOT NULL,
    title TEXT NOT NULL,
    notes TEXT,
    credit_amount INTEGER DEFAULT 0,
    debit_amount INTEGER DEFAULT 0,
    timestamp_epoch_ms INTEGER NOT NULL,
    category_id INTEGER,
    FOREIGN KEY (project_id) REFERENCES projects (id)
);
```

## 🎯 Features in Detail

### **Transaction Management**
- **CRUD Operations**: Create, Read, Update, Delete transactions
- **Search Functionality**: Search by title or amount
- **Date Editing**: Editable transaction dates
- **Amount Formatting**: Indian numbering system (34,56,789)
- **Transaction Validation**: Input validation and error handling

### **Project Management**
- **Multi-Project**: Support for unlimited projects
- **Project Descriptions**: Detailed project information
- **Project Trash**: Safe project deletion with data cleanup
- **Project Export**: Individual project data export

### **Financial Analytics**
- **Real-time Calculations**: Automatic balance updates
- **Visual Indicators**: Color-coded credit/debit amounts
- **Running Balances**: Transaction-by-transaction balance tracking
- **Summary Views**: Project-level financial summaries

### **Data Export**
- **CSV Format**: Standard CSV export format
- **Date-Only**: Clean exports without time columns
- **Comprehensive Data**: Complete transaction history
- **Share Integration**: Native Android sharing

## 🔒 Privacy & Security

### **Data Protection**
- **Local Storage**: All data stored locally
- **No Cloud Sync**: No external data transmission
- **User Control**: Complete control over data
- **Export Only**: Data sharing only when user initiates

### **Permissions**
- **Storage**: Required for CSV export functionality
- **Notifications**: Optional for export notifications
- **No Network**: No internet permissions required

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

**Raghram K S**
- **GitHub**: [@raghramks](https://github.com/raghramks)
- **Project**: [Accounts Manager Application](https://github.com/papilo-topato/AccountsManagerApplication)
- **Version**: v1.0 (Completed)

## 🙏 Acknowledgments

- **Jetpack Compose** team for the amazing UI framework
- **Material Design** team for the design system
- **Android** community for continuous support
- **Open source contributors** who made this possible

## 📈 Roadmap

### **Version 1.1 (Planned)**
- [ ] Custom categories management
- [ ] Bulk operations (multi-select, bulk delete)
- [ ] Advanced search filters
- [ ] Data backup and restore
- [ ] Widget support

### **Version 1.2 (Future)**
- [ ] Advanced reporting and analytics
- [ ] Multi-currency support
- [ ] Budget tracking features
- [ ] Cloud synchronization
- [ ] Wear OS companion app

---

<div align="center">

**⭐ Star this repository if you find it helpful!**

[![GitHub stars](https://img.shields.io/github/stars/papilo-topato/AccountsManagerApplication?style=social)](https://github.com/papilo-topato/AccountsManagerApplication/stargazers)

**Made with ❤️ by [Raghram K S](https://github.com/raghramks)**

</div>