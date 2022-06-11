package com.example.eshop.ui.fragments

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.eshop.R
import com.example.eshop.data.local.datastore.lastproduct.LastProductInfo
import com.example.eshop.data.repository.ProductRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class NotifyWork @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val productRepository: ProductRepository,
    private val lastProductDataStore: LastProductInfo
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        getLastLocalProduct()
        return Result.success()
    }

    private suspend fun getLastLocalProduct() {
        lastProductDataStore.preferences.collect {
            if (getLastRemoteProduct() != it) {
                lastProductDataStore.saveUserInfo(getLastRemoteProduct())
                createNotification("salam", getLastRemoteProduct().toString())
            }
        }
    }

    private suspend fun getLastRemoteProduct(): Int {
        val newProducts = productRepository.getLastProduct(1, 1)
        return newProducts.body()?.get(0)?.id ?: 0
    }

    private fun createNotification(title: String, description: String) {

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel("101", "channel", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notificationBuilder = NotificationCompat.Builder(applicationContext, "101")
            .setContentTitle(title)
            .setContentText(description)
            .setSmallIcon(R.drawable.ic_launcher_background)

        notificationManager.notify(1, notificationBuilder.build())

    }
}