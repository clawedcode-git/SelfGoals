package com.example.selfgoals.ui.dashboard

data class ProgressStats(
    val totalGoals: Int = 0,
    val completedGoals: Int = 0,
    val totalMilestones: Int = 0,
    val completedMilestones: Int = 0,
    val categoryStats: Map<String, Float> = emptyMap() // Category Name to Completion Percentage
) {
    val goalCompletionRate: Float = if (totalGoals > 0) completedGoals.toFloat() / totalGoals else 0f
    val milestoneCompletionRate: Float = if (totalMilestones > 0) completedMilestones.toFloat() / totalMilestones else 0f
}
