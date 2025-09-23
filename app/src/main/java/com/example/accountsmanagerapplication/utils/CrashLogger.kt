package com.example.accountsmanagerapplication.utils

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

object CrashLogger {
    private const val TAG = "CrashLogger"
    private const val LOG_DIR = "crash_logs"
    private const val MAX_LOG_FILES = 10 // Keep only last 10 crash logs
    
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
    
    /**
     * Log a crash with stack trace
     */
    fun logCrash(context: Context, throwable: Throwable, additionalInfo: String = "") {
        try {
            val timestamp = System.currentTimeMillis()
            val logContent = buildCrashLog(timestamp, throwable, additionalInfo)
            
            // Log to Android logcat
            Log.e(TAG, "CRASH DETECTED", throwable)
            
            // Save to file
            saveCrashLog(context, timestamp, logContent)
            
        } catch (e: Exception) {
            Log.e(TAG, "Failed to log crash", e)
        }
    }
    
    /**
     * Log a general error (non-crash)
     */
    fun logError(context: Context, message: String, throwable: Throwable? = null) {
        try {
            val timestamp = System.currentTimeMillis()
            val logContent = buildErrorLog(timestamp, message, throwable)
            
            // Log to Android logcat
            Log.e(TAG, message, throwable)
            
            // Save to file
            saveErrorLog(context, timestamp, logContent)
            
        } catch (e: Exception) {
            Log.e(TAG, "Failed to log error", e)
        }
    }
    
    /**
     * Log general app information
     */
    fun logInfo(context: Context, message: String) {
        try {
            val timestamp = System.currentTimeMillis()
            val logContent = buildInfoLog(timestamp, message)
            
            // Log to Android logcat
            Log.i(TAG, message)
            
            // Save to file
            saveInfoLog(context, timestamp, logContent)
            
        } catch (e: Exception) {
            Log.e(TAG, "Failed to log info", e)
        }
    }
    
    private fun buildCrashLog(timestamp: Long, throwable: Throwable, additionalInfo: String): String {
        val sb = StringBuilder()
        sb.appendLine("=== CRASH REPORT ===")
        sb.appendLine("Timestamp: ${dateFormat.format(Date(timestamp))}")
        sb.appendLine("App Version: ${getAppVersion()}")
        sb.appendLine("Android Version: ${android.os.Build.VERSION.RELEASE}")
        sb.appendLine("Device: ${android.os.Build.MANUFACTURER} ${android.os.Build.MODEL}")
        sb.appendLine("SDK Level: ${android.os.Build.VERSION.SDK_INT}")
        sb.appendLine("Additional Info: $additionalInfo")
        sb.appendLine()
        sb.appendLine("Exception: ${throwable.javaClass.simpleName}")
        sb.appendLine("Message: ${throwable.message}")
        sb.appendLine()
        sb.appendLine("Stack Trace:")
        sb.appendLine(throwable.stackTraceToString())
        sb.appendLine("=== END CRASH REPORT ===")
        sb.appendLine()
        
        return sb.toString()
    }
    
    private fun buildErrorLog(timestamp: Long, message: String, throwable: Throwable?): String {
        val sb = StringBuilder()
        sb.appendLine("=== ERROR LOG ===")
        sb.appendLine("Timestamp: ${dateFormat.format(Date(timestamp))}")
        sb.appendLine("App Version: ${getAppVersion()}")
        sb.appendLine("Android Version: ${android.os.Build.VERSION.RELEASE}")
        sb.appendLine("Device: ${android.os.Build.MANUFACTURER} ${android.os.Build.MODEL}")
        sb.appendLine("SDK Level: ${android.os.Build.VERSION.SDK_INT}")
        sb.appendLine("Error: $message")
        if (throwable != null) {
            sb.appendLine("Exception: ${throwable.javaClass.simpleName}")
            sb.appendLine("Message: ${throwable.message}")
            sb.appendLine("Stack Trace:")
            sb.appendLine(throwable.stackTraceToString())
        }
        sb.appendLine("=== END ERROR LOG ===")
        sb.appendLine()
        
        return sb.toString()
    }
    
    private fun buildInfoLog(timestamp: Long, message: String): String {
        val sb = StringBuilder()
        sb.appendLine("=== INFO LOG ===")
        sb.appendLine("Timestamp: ${dateFormat.format(Date(timestamp))}")
        sb.appendLine("App Version: ${getAppVersion()}")
        sb.appendLine("Info: $message")
        sb.appendLine("=== END INFO LOG ===")
        sb.appendLine()
        
        return sb.toString()
    }
    
    private fun saveCrashLog(context: Context, timestamp: Long, content: String) {
        val fileName = "crash_${timestamp}.log"
        saveLogToFile(context, fileName, content)
    }
    
    private fun saveErrorLog(context: Context, timestamp: Long, content: String) {
        val fileName = "error_${timestamp}.log"
        saveLogToFile(context, fileName, content)
    }
    
    private fun saveInfoLog(context: Context, timestamp: Long, content: String) {
        val fileName = "info_${timestamp}.log"
        saveLogToFile(context, fileName, content)
    }
    
    private fun saveLogToFile(context: Context, fileName: String, content: String) {
        try {
            val logDir = File(context.filesDir, LOG_DIR)
            if (!logDir.exists()) {
                logDir.mkdirs()
            }
            
            val logFile = File(logDir, fileName)
            FileWriter(logFile).use { writer ->
                writer.write(content)
            }
            
            // Clean up old log files
            cleanupOldLogs(logDir)
            
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save log to file", e)
        }
    }
    
    private fun cleanupOldLogs(logDir: File) {
        try {
            val logFiles = logDir.listFiles()?.sortedByDescending { it.lastModified() }
            if (logFiles != null && logFiles.size > MAX_LOG_FILES) {
                val filesToDelete = logFiles.drop(MAX_LOG_FILES)
                filesToDelete.forEach { it.delete() }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to cleanup old logs", e)
        }
    }
    
    /**
     * Get all crash log files
     */
    suspend fun getCrashLogFiles(context: Context): List<File> = withContext(Dispatchers.IO) {
        try {
            val logDir = File(context.filesDir, LOG_DIR)
            if (!logDir.exists()) return@withContext emptyList()
            
            logDir.listFiles()?.filter { it.name.startsWith("crash_") }?.sortedByDescending { it.lastModified() } ?: emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get crash log files", e)
            emptyList()
        }
    }
    
    /**
     * Get all log files (crash, error, info)
     */
    suspend fun getAllLogFiles(context: Context): List<File> = withContext(Dispatchers.IO) {
        try {
            val logDir = File(context.filesDir, LOG_DIR)
            if (!logDir.exists()) return@withContext emptyList()
            
            logDir.listFiles()?.sortedByDescending { it.lastModified() } ?: emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get all log files", e)
            emptyList()
        }
    }
    
    /**
     * Read log file content
     */
    suspend fun readLogFile(file: File): String = withContext(Dispatchers.IO) {
        try {
            file.readText()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to read log file", e)
            "Error reading log file: ${e.message}"
        }
    }
    
    /**
     * Delete all log files
     */
    suspend fun clearAllLogs(context: Context) = withContext(Dispatchers.IO) {
        try {
            val logDir = File(context.filesDir, LOG_DIR)
            if (logDir.exists()) {
                logDir.listFiles()?.forEach { it.delete() }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to clear logs", e)
        }
    }
    
    private fun getAppVersion(): String {
        return try {
            "1.0" // You can get this from BuildConfig.VERSION_NAME
        } catch (e: Exception) {
            "Unknown"
        }
    }
}
