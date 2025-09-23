package com.example.accountsmanagerapplication.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.accountsmanagerapplication.data.dao.CategoryDao
import com.example.accountsmanagerapplication.data.dao.ProjectDao
import com.example.accountsmanagerapplication.data.dao.TransactionDao

@Database(
    entities = [ProjectEntity::class, TransactionEntity::class, CategoryEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
}


