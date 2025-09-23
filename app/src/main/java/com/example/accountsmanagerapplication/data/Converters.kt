package com.example.accountsmanagerapplication.data

import androidx.room.TypeConverter
import java.util.Date

class Converters {
    @TypeConverter
    fun fromEpoch(ms: Long?): Date? = ms?.let { Date(it) }

    @TypeConverter
    fun dateToEpoch(date: Date?): Long? = date?.time
}



