package com.example.accountsmanagerapplication.util

import com.example.accountsmanagerapplication.data.ProjectEntity
import com.example.accountsmanagerapplication.data.TransactionEntity
import com.example.accountsmanagerapplication.data.dao.ProjectBalanceRow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object CsvExportUtil {
    private val dateFmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun generateAllProjectsCsv(
        projects: List<ProjectBalanceRow>,
        transactionsByProject: Map<Long, List<TransactionEntity>>
    ): String {
        val sb = StringBuilder()
        sb.appendLine("Project Name,Date,Title,Category,Credit,Debit,Running Balance")
        projects.forEach { project ->
            val txns = (transactionsByProject[project.projectId] ?: emptyList())
                .sortedBy { it.timestampEpochMs }
            var runningMinor = 0L
            txns.forEach { t ->
                runningMinor += (t.creditAmount - t.debitAmount)
                val date = Date(t.timestampEpochMs)
                val credit = minorToDecimalString(t.creditAmount)
                val debit = minorToDecimalString(t.debitAmount)
                val running = minorToDecimalString(runningMinor)
                sb.appendLine(
                    listOf(
                        csv(project.name),
                        dateFmt.format(date),
                        csv(t.title),
                        csv("") /* category name not implemented yet */, 
                        credit,
                        debit,
                        running
                    ).joinToString(",")
                )
            }
        }
        return sb.toString()
    }

    fun generateSingleProjectCsv(
        project: ProjectEntity,
        transactions: List<TransactionEntity>
    ): String {
        val header = "Date,Title,Category,Credit,Debit,Running Balance"
        var runningBalance = 0L // Stored as minor units (cents/paise)

        val rows = transactions.sortedBy { it.timestampEpochMs }.map { txn ->
            val creditAmount = txn.creditAmount
            val debitAmount = txn.debitAmount
            runningBalance += creditAmount - debitAmount

            val date = dateFmt.format(Date(txn.timestampEpochMs))
            val title = "\"${txn.title.replace("\"", "\"\"")}\"" // Escape quotes
            val category = "\"${txn.categoryId?.toString() ?: ""}\""
            val credit = String.format(java.util.Locale.US, "%.2f", creditAmount / 100.0)
            val debit = String.format(java.util.Locale.US, "%.2f", debitAmount / 100.0)
            val balance = String.format(java.util.Locale.US, "%.2f", runningBalance / 100.0)

            "$date,$title,$category,$credit,$debit,$balance"
        }

        return buildString {
            appendLine(header)
            rows.forEach { appendLine(it) }
        }
    }

    private fun minorToDecimalString(minor: Long): String {
        val sign = if (minor < 0) "-" else ""
        val abs = kotlin.math.abs(minor)
        val rupees = abs / 100
        val paise = abs % 100
        return "$sign$rupees.${paise.toString().padStart(2, '0')}"
    }

    private fun csv(value: String): String {
        val needsQuotes = value.contains(',') || value.contains('\n') || value.contains('"')
        val escaped = value.replace("\"", "\"\"")
        return if (needsQuotes) "\"$escaped\"" else escaped
    }
}


