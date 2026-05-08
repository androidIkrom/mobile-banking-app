package com.example.entity.network

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.entity.local.NetworkLogDao
import com.example.entity.local.NetworkLogEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.nio.charset.Charset
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkLogInterceptor @Inject constructor(
    @ApplicationContext private val context: Context, private val networkLogDao: NetworkLogDao
) : Interceptor {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val channelId = "network_logs"
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val startTime = System.currentTimeMillis()
        val requestBodyString = request.body?.let { body ->
            val buffer = Buffer()
            body.writeTo(buffer)
            buffer.readString(Charset.forName("UTF-8"))
        }
        val response: Response = try {
            chain.proceed(request)
        } catch (e: Exception) {
            val entity = NetworkLogEntity(
                method = request.method,
                url = request.url.toString(),
                code = "Error",
                requestBody = requestBodyString,
                responseBody = e.message ?: "Unknown Error",
                message = "Connection Failed",
                timeStamp = System.currentTimeMillis(),
                duration = System.currentTimeMillis() - startTime
            )
            saveLog(entity)
            throw e
        }
        val endTime = System.currentTimeMillis()
        val durationTime = endTime - startTime
        val responseBodyString = response.body?.let { body ->
            val source = body.source()
            source.request(Long.MAX_VALUE)
            val buffer = source.buffer
            buffer.clone().readString(Charset.forName("UTF-8"))
        } ?: "No response"

        val logEntry = NetworkLogEntity(
            method = request.method,
            url = request.url.toString(),
            code = response.code.toString(),
            requestBody = requestBodyString,
            responseBody = responseBodyString,
            message = response.message,
            timeStamp = endTime,
            duration = durationTime
        )
        saveLog(logEntry)
        sendNotification(logEntry)
        return response
    }

    private fun saveLog(log: NetworkLogEntity) {
        coroutineScope.launch {
            networkLogDao.insertLog(log)
        }
    }

    private fun sendNotification(log: NetworkLogEntity) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            if (notificationManager.getNotificationChannel(channelId) == null){
                val name = "Network Logs"
                val channel = NotificationChannel(channelId,name, NotificationManager.IMPORTANCE_HIGH)
                notificationManager.createNotificationChannel(channel)
            }
        }
        val intent = Intent().apply {
            setClassName(
                context.packageName,
                "com.example.zoomrad.presentation.screens.chucker.MyChuckerActivity"
            )
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.stat_notify_sync)
            .setContentTitle("Http ${log.code}: ${log.method}").setContentText(log.url)
            .setAutoCancel(true)
            .setStyle(NotificationCompat.BigTextStyle().bigText("message: ${log.message}"))
            .setPriority(NotificationCompat.PRIORITY_HIGH).setContentIntent(pendingIntent).build()
        notificationManager.notify(
            log.timeStamp.toInt(), notification
        )
    }

//    private fun createNotificationChannel() {
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
//            val name = "Network Logs"
//            val channel = NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_HIGH)
//            val notificationManager =
//                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }
//    }
}
