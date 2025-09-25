package com.example.accountsmanagerapplication.util

fun formatCurrencyMinor(amountMinor: Long): String {
    val isNegative = amountMinor < 0
    val abs = kotlin.math.abs(amountMinor)
    val rupees = abs / 100
    val paise = abs % 100
    val formattedRupees = formatIndianNumber(rupees)
    val base = "₹$formattedRupees.%02d".format(paise)
    return if (isNegative) "-$base" else base
}

/**
 * Formats a number using Indian numbering system (lakhs, crores)
 * Example: 3456789 -> 34,56,789
 */
private fun formatIndianNumber(number: Long): String {
    if (number == 0L) return "0"
    
    val numberStr = number.toString()
    val length = numberStr.length
    
    return when {
        length <= 3 -> numberStr
        length <= 5 -> {
            val lastThree = numberStr.substring(length - 3)
            val firstPart = numberStr.substring(0, length - 3)
            "$firstPart,$lastThree"
        }
        length <= 7 -> {
            val lastThree = numberStr.substring(length - 3)
            val middleTwo = numberStr.substring(length - 5, length - 3)
            val firstPart = numberStr.substring(0, length - 5)
            "$firstPart,$middleTwo,$lastThree"
        }
        else -> {
            // For numbers >= 8 digits (crores and above)
            val lastThree = numberStr.substring(length - 3)
            val middleTwo = numberStr.substring(length - 5, length - 3)
            val nextTwo = numberStr.substring(length - 7, length - 5)
            val firstPart = numberStr.substring(0, length - 7)
            "$firstPart,$nextTwo,$middleTwo,$lastThree"
        }
    }
}



