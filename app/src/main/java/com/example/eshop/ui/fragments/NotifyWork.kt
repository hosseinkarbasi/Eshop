package com.example.eshop.ui.fragments

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.eshop.R
import com.example.eshop.data.local.datastore.lastproduct.LastProductInfo
import com.example.eshop.data.repository.ProductRepository
import com.example.eshop.ui.activity.MainActivity
import com.example.eshop.utils.vectorToBitmap
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
                createNotification(getLastRemoteProduct())
            }
        }
    }

    private suspend fun getLastRemoteProduct(): Int {
        val newProducts = productRepository.getLastProduct(1, 1)
        return newProducts.body()?.get(0)?.id ?: 0
    }

    private fun createNotification(productId: Int) {

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel("101", "channel", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val bundle = Bundle()
        bundle.putInt("product_id", productId)

        val pendingIntent = NavDeepLinkBuilder(applicationContext)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.productFragment)
            .setArguments(bundle)
            .createPendingIntent()

        val bitmap = applicationContext.vectorToBitmap(R.drawable.larg_icon_notif)
        val notificationBuilder = NotificationCompat.Builder(applicationContext, "101")
            .setContentTitle("محصول جدید")
            .setContentText("برای دیدن محصول جدید کلیک کن")
            .setLargeIcon(bitmap)
            .setSmallIcon(R.drawable.small_icon_notif)
            .setContentIntent(pendingIntent)

        notificationManager.notify(1, notificationBuilder.build())

    }
}