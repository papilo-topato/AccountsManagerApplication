package com.example.accountsmanagerapplication.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.accountsmanagerapplication.R

class ExporterService(private val context: Context) {

    fun exportProject(projectName: String, csvContent: String) {
        val fileName = "${projectName.replace(" ", "_")}_export.csv"

        // 1. Save the file to the Downloads folder
        val uri = saveCsvToDownloads(fileName, csvContent)

        if (uri != null) {
            // 2. Show a notification
            showSaveNotification(fileName, uri)

            // 3. Launch the share sheet
            launchShareIntent(uri)
        } else {
            Toast.makeText(context, "Failed to save file.", Toast.LENGTH_LONG).show()
        }
    }

    private fun saveCsvToDownloads(fileName: String, content: String): Uri? {
        val contentResolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Download/AccountsManager")
            }
        }

        val uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
        uri?.let {
            try {
                contentResolver.openOutputStream(it)?.use { outputStream ->
                    outputStream.write(content.toByteArray())
                }
                return it
            } catch (e: Exception) {
                android.util.Log.e("ExporterService", "Failed to write file", e)
            }
        }
        return null
    }

    private fun showSaveNotification(fileName: String, uri: Uri) {
        val notificationManager = NotificationManagerCompat.from(context)
        val notification = NotificationCompat.Builder(context, "export_channel_id")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Export Successful")
            .setContentText("$fileName saved to Downloads.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }

    private fun launchShareIntent(uri: Uri) {
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "text/csv"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share Exported File"))
    }
}

