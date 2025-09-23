package com.example.accountsmanagerapplication

import android.app.Application
import android.util.Log
import com.example.accountsmanagerapplication.utils.CrashLogger

class AccountsManagerApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Set up global exception handler
        setupCrashHandler()
        
        // Log app startup
        CrashLogger.logInfo(this, "App started successfully")
        
        Log.d("AppStartup", "AccountsManagerApplication.onCreate() called")
    }
    
    private fun setupCrashHandler() {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            try {
                // Log the crash
                CrashLogger.logCrash(
                    context = this,
                    throwable = throwable,
                    additionalInfo = "Uncaught exception in thread: ${thread.name}"
                )
                
                Log.e("CrashHandler", "Uncaught exception logged", throwable)
                
            } catch (e: Exception) {
                Log.e("CrashHandler", "Failed to log crash", e)
            } finally {
                // Call the default handler to show crash dialog
                defaultHandler?.uncaughtException(thread, throwable)
            }
        }
    }
}
