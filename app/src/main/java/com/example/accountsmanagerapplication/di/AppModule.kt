package com.example.accountsmanagerapplication.di

import android.content.Context
import androidx.room.Room
import com.example.accountsmanagerapplication.data.AppDatabase
import com.example.accountsmanagerapplication.data.dao.CategoryDao
import com.example.accountsmanagerapplication.data.dao.ProjectDao
import com.example.accountsmanagerapplication.data.dao.TransactionDao
import com.example.accountsmanagerapplication.data.repo.CategoryRepository
import com.example.accountsmanagerapplication.data.repo.ProjectRepository
import com.example.accountsmanagerapplication.data.repo.TransactionRepository

object AppModule {
    @Volatile
    private var database: AppDatabase? = null

    fun provideDatabase(context: Context): AppDatabase {
        return database ?: synchronized(this) {
            database ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "accounts_manager.db"
            ).build().also { database = it }
        }
    }

    fun provideProjectDao(db: AppDatabase): ProjectDao = db.projectDao()

    fun provideTransactionDao(db: AppDatabase): TransactionDao = db.transactionDao()

    fun provideCategoryDao(db: AppDatabase): CategoryDao = db.categoryDao()

    fun provideProjectRepository(projectDao: ProjectDao): ProjectRepository =
        ProjectRepository(projectDao)

    fun provideTransactionRepository(transactionDao: TransactionDao): TransactionRepository =
        TransactionRepository(transactionDao)

    fun provideCategoryRepository(categoryDao: CategoryDao): CategoryRepository =
        CategoryRepository(categoryDao)
}



