package com.example.accountsmanagerapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.accountsmanagerapplication.ui.theme.ACCOUNTSMANAGERAPPLICATIONTheme
import com.example.accountsmanagerapplication.ui.theme.LocalThemeState
import com.example.accountsmanagerapplication.ui.theme.ThemeStateManager
import com.example.accountsmanagerapplication.navigation.AppNavHost
import com.example.accountsmanagerapplication.ui.SplashScreen
import com.example.accountsmanagerapplication.utils.CrashLogger

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("AppStartup", "MainActivity.onCreate() called")
        
        try {
            enableEdgeToEdge()
            CrashLogger.logInfo(this, "MainActivity started successfully")
        } catch (e: Exception) {
            CrashLogger.logError(this, "Failed to initialize MainActivity", e)
        }
        setContent {
            val themeStateManager = remember { ThemeStateManager() }
            var showSplash by remember { mutableStateOf(true) }
            
            androidx.compose.runtime.CompositionLocalProvider(LocalThemeState provides themeStateManager) {
                ACCOUNTSMANAGERAPPLICATIONTheme(darkTheme = themeStateManager.isDarkTheme) {
                    if (showSplash) {
                        SplashScreen(
                            onLoadingComplete = { showSplash = false }
                        )
                    } else {
                        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                            AppNavHost()
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ACCOUNTSMANAGERAPPLICATIONTheme {
        AppNavHost()
    }
}