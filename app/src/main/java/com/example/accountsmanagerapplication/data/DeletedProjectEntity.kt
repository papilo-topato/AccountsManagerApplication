package com.example.accountsmanagerapplication.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(
    tableName = "deleted_projects",
    indices = [Index(value = ["original_id"], unique = true)]
)
@Serializable
data class DeletedProjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "original_id")
    val originalId: Long,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "description")
    val description: String? = null,
    @ColumnInfo(name = "created_at_epoch_ms")
    val createdAtEpochMs: Long,
    @ColumnInfo(name = "deleted_at_epoch_ms")
    val deletedAtEpochMs: Long = System.currentTimeMillis()
)

