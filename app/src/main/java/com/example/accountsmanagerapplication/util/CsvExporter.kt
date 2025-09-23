package com.example.accountsmanagerapplication.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileWriter

object CsvExporter {
    fun exportToCache(context: Context, fileName: String = "accounts_export.csv", rows: List<List<String>>): Uri {
        val cacheDir = File(context.cacheDir, "exports").apply { mkdirs() }
        val file = File(cacheDir, fileName)
        FileWriter(file, false).use { writer ->
            rows.forEach { row ->
                val escaped = row.joinToString(",") { escapeCsv(it) }
                writer.appendLine(escaped)
            }
        }
        return FileProvider.getUriForFile(
            context,
            context.packageName + ".fileprovider",
            file
        )
    }

    private fun escapeCsv(input: String): String {
        val needsQuotes = input.contains(',') || input.contains('\n') || input.contains('"')
        var content = input.replace("\"", "\"\"")
        return if (needsQuotes) "\"$content\"" else content
    }
}



