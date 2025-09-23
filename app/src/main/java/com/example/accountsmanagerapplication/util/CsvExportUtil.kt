package com.example.accountsmanagerapplication.util

import com.example.accountsmanagerapplication.data.ProjectEntity
import com.example.accountsmanagerapplication.data.TransactionEntity
import com.example.accountsmanagerapplication.data.dao.ProjectBalanceRow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object CsvExportUtil {
    private val dateFmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val timeFmt = SimpleDateFormat("HH:mm", Locale.getDefault())

    fun generateAllProjectsCsv(
        projects: List<ProjectBalanceRow>,
        transactionsByProject: Map<Long, List<TransactionEntity>>
    ): String {
        val sb = StringBuilder()
        sb.appendLine("Project Name,Date,Time,Title,Category,Credit,Debit,Running Balance")
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
                        timeFmt.format(date),
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
        val sb = StringBuilder()
        sb.appendLine("Date,Time,Title,Category,Credit,Debit,Running Balance")
        var runningMinor = 0L
        transactions.sortedBy { it.timestampEpochMs }.forEach { t ->
            runningMinor += (t.creditAmount - t.debitAmount)
            val date = Date(t.timestampEpochMs)
            val credit = minorToDecimalString(t.creditAmount)
            val debit = minorToDecimalString(t.debitAmount)
            val running = minorToDecimalString(runningMinor)
            sb.appendLine(
                listOf(
                    dateFmt.format(date),
                    timeFmt.format(date),
                    csv(t.title),
                    csv("") /* category name not implemented yet */, 
                    credit,
                    debit,
                    running
                ).joinToString(",")
            )
        }
        return sb.toString()
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


