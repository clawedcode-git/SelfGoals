package com.example.selfgoals.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "goals",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("categoryId")]
)
data class Goal(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val categoryId: Long?,
    val deadline: Long?, // Timestamp
    val reminderTime: Long? = null, // Timestamp for notification
    val isCompleted: Boolean = false,
    val isArchived: Boolean = false,
    val isPriority: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
