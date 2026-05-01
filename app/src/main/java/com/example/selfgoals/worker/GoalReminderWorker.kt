package com.example.selfgoals.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.selfgoals.utils.NotificationHelper

class GoalReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val goalTitle = inputData.getString("goal_title") ?: "Goal Reminder"
        val goalId = inputData.getLong("goal_id", 0).toInt()

        val notificationHelper = NotificationHelper(applicationContext)
        notificationHelper.createNotificationChannel()
        notificationHelper.showNotification(
            title = "Time to work on your goal!",
            message = goalTitle,
            notificationId = goalId
        )

        return Result.success()
    }
}
