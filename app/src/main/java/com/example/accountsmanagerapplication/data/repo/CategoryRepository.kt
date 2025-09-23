package com.example.accountsmanagerapplication.data.repo

import com.example.accountsmanagerapplication.data.CategoryEntity
import com.example.accountsmanagerapplication.data.dao.CategoryDao
import kotlinx.coroutines.flow.Flow

class CategoryRepository(
    private val categoryDao: CategoryDao
) {
    fun observeCategories(): Flow<List<CategoryEntity>> = categoryDao.observeAll()

    suspend fun addCategory(name: String): Long = categoryDao.insert(CategoryEntity(name = name))

    suspend fun deleteCategory(id: Long) = categoryDao.deleteById(id)
}



