package com.example.accountsmanagerapplication.util

fun formatCurrencyMinor(amountMinor: Long): String {
    val isNegative = amountMinor < 0
    val abs = kotlin.math.abs(amountMinor)
    val rupees = abs / 100
    val paise = abs % 100
    val base = "₹%d.%02d".format(rupees, paise)
    return if (isNegative) "-$base" else base
}



