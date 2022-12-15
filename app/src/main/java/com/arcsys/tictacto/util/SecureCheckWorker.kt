package com.arcsys.tictacto.util

import android.content.Context
import android.provider.Settings
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.arcsys.tictacto.CloverApplication

class SecureCheckWorker(context: Context, workerParam: WorkerParameters) :
    Worker(context, workerParam) {
    override fun doWork(): Result {
        val isSuccess = checkApp(applicationContext)
        val outputData = workDataOf("isSuccess" to isSuccess)
        return Result.success(outputData)
    }

    private fun checkApp(context: Context): Boolean {
        return Settings.Global.getString(
            context.contentResolver,
            Settings.Global.ADB_ENABLED
        ) == "1"
    }
}