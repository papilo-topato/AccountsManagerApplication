# Contributing to Accounts Manager Application

Thank you for your interest in contributing to the Accounts Manager Application! This document provides guidelines and information for contributors.

## 🚀 Getting Started

### Prerequisites
- Android Studio Arctic Fox (2020.3.1) or later
- Android SDK 26 (Android 8.0) or higher
- Kotlin 1.9.0 or later
- Git

### Development Setup
1. Fork the repository
2. Clone your fork:
   ```bash
   git clone https://github.com/YOUR_USERNAME/AccountsManagerApplication.git
   cd AccountsManagerApplication
   ```
3. Open the project in Android Studio
4. Wait for Gradle sync to complete
5. Build and run the project

## 📋 How to Contribute

### Reporting Bugs
1. **Check existing issues** first to avoid duplicates
2. **Use the app's crash log viewer** to get detailed error information
3. **Create a new issue** with the following information:
   - **Bug description**: Clear, concise description
   - **Steps to reproduce**: Detailed steps to reproduce the issue
   - **Expected behavior**: What should happen
   - **Actual behavior**: What actually happens
   - **Device information**: Android version, device model, app version
   - **Screenshots**: If applicable
   - **Crash logs**: If available from the app's log viewer

### Suggesting Features
1. **Check existing feature requests** first
2. **Create a new issue** with the "enhancement" label
3. **Include the following**:
   - **Feature description**: Clear description of the proposed feature
   - **Use case**: Why this feature would be useful
   - **Mockups/wireframes**: If applicable
   - **Implementation ideas**: If you have any

### Code Contributions

#### Pull Request Process
1. **Create a feature branch**:
   ```bash
   git checkout -b feature/your-feature-name
   ```

2. **Make your changes** following the coding standards below

3. **Test your changes**:
   ```bash
   ./gradlew test
   ./gradlew connectedAndroidTest
   ```

4. **Commit your changes**:
   ```bash
   git commit -m "Add: Brief description of your changes"
   ```

5. **Push to your fork**:
   ```bash
   git push origin feature/your-feature-name
   ```

6. **Create a Pull Request** with:
   - Clear title and description
   - Reference any related issues
   - Screenshots if UI changes are involved
   - Testing instructions

## 📝 Coding Standards

### Kotlin Style
- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable and function names
- Add KDoc comments for public functions
- Keep functions small and focused

### Compose Guidelines
- Use `@Composable` functions for UI components
- Follow Material Design 3 principles
- Use proper state management with `remember` and `mutableStateOf`
- Implement proper accessibility support

### Architecture
- Follow MVVM pattern
- Use Repository pattern for data access
- Implement proper error handling
- Use Coroutines for asynchronous operations

### Example Code Structure
```kotlin
/**
 * Brief description of the composable
 * @param parameter Description of parameter
 * @return Description of return value
 */
@Composable
fun ExampleComposable(
    parameter: String,
    modifier: Modifier = Modifier,
    onAction: () -> Unit = {}
) {
    // Implementation
}
```

## 🧪 Testing

### Unit Tests
- Write unit tests for ViewModels and business logic
- Use descriptive test names
- Follow AAA pattern (Arrange, Act, Assert)

### UI Tests
- Test critical user flows
- Use Compose testing APIs
- Test both light and dark themes

### Example Test
```kotlin
@Test
fun `when user adds transaction, balance should update correctly`() {
    // Arrange
    val viewModel = TransactionViewModel(repository)
    val initialBalance = 1000L
    
    // Act
    viewModel.addTransaction(amount = 500L, type = TransactionType.INCOME)
    
    // Assert
    assertEquals(1500L, viewModel.currentBalance.value)
}
```

## 📱 UI/UX Guidelines

### Design Principles
- Follow Material Design 3 guidelines
- Ensure accessibility compliance
- Support both light and dark themes
- Maintain consistent spacing and typography

### Component Guidelines
- Use Material 3 components when possible
- Implement proper loading states
- Handle error states gracefully
- Provide user feedback for actions

### Accessibility
- Add content descriptions for icons
- Ensure proper contrast ratios
- Support screen readers
- Implement proper focus management

## 🔧 Development Workflow

### Branch Naming
- `feature/feature-name` - New features
- `bugfix/issue-description` - Bug fixes
- `hotfix/critical-issue` - Critical fixes
- `docs/documentation-update` - Documentation updates

### Commit Messages
Use conventional commit format:
- `feat: add new feature`
- `fix: resolve bug in transaction calculation`
- `docs: update README with new features`
- `refactor: improve code structure`
- `test: add unit tests for ViewModel`

### Code Review Process
1. **Self-review** your changes before submitting
2. **Ensure tests pass** and coverage is maintained
3. **Update documentation** if needed
4. **Respond to feedback** promptly
5. **Make requested changes** and re-request review

## 🐛 Debugging

### Using the Built-in Log Viewer
1. Open the app
2. Tap the bug report icon (🐛) in the dashboard
3. View crash logs and device information
4. Share logs when reporting issues

### Common Issues
- **Build failures**: Check Gradle sync and dependencies
- **Runtime crashes**: Use the app's crash log viewer
- **UI issues**: Test on different screen sizes and orientations
- **Performance issues**: Use Android Studio Profiler

## 📚 Resources

### Documentation
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Material Design 3](https://m3.material.io/)
- [Android Architecture Components](https://developer.android.com/topic/architecture)
- [Room Database](https://developer.android.com/training/data-storage/room)

### Tools
- [Android Studio](https://developer.android.com/studio)
- [Kotlin Playground](https://play.kotlinlang.org/)
- [Material Design Tools](https://m3.material.io/theme-builder)

## 🤝 Community Guidelines

### Be Respectful
- Use welcoming and inclusive language
- Be respectful of differing viewpoints and experiences
- Accept constructive criticism gracefully
- Focus on what is best for the community

### Be Collaborative
- Help others learn and grow
- Share knowledge and resources
- Provide constructive feedback
- Work together to solve problems

## 📞 Getting Help

### Questions and Support
- **GitHub Issues**: For bugs and feature requests
- **GitHub Discussions**: For questions and general discussion
- **Pull Request Comments**: For code-specific questions

### Contact
- **Maintainer**: [@papilo-topato](https://github.com/papilo-topato)
- **Repository**: [Accounts Manager Application](https://github.com/papilo-topato/AccountsManagerApplication)

## 🎉 Recognition

Contributors will be recognized in:
- **README.md** contributors section
- **Release notes** for significant contributions
- **GitHub contributors** page

Thank you for contributing to the Accounts Manager Application! 🚀
