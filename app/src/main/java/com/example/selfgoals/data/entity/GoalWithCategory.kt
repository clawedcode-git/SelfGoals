package com.example.selfgoals.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class GoalWithCategory(
    @Embedded val goal: Goal,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: Category?
)
