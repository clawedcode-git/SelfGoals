package com.example.selfgoals.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class GoalDetails(
    @Embedded val goal: Goal,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: Category?,
    @Relation(
        parentColumn = "id",
        entityColumn = "goalId"
    )
    val milestones: List<Milestone>
)
