package com.example.musicapp.presentation.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.musicapp.R

class MyService : Service() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var notification: Notification

    override fun onCreate() {
        super.onCreate()

        mediaPlayer = MediaPlayer.create(this, R.raw.turk_music_1)
        notification = NotificationCompat
            .Builder(this, "test123")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("What's up!")
            .setContentText("Hello bro")
            .build()

        makeChannel()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mediaPlayer.start()
        startForeground(1, notification)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun makeChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel("test1", "abc", NotificationManager.IMPORTANCE_HIGH)
            val notificationManager = NotificationManagerCompat.from(this)
            notificationManager.createNotificationChannel(notificationChannel)
        }

    }

}