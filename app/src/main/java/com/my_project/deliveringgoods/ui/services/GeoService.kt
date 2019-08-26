package com.my_project.deliveringgoods.ui.services


import CONST
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Handler
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.my_project.deliveringgoods.App
import com.my_project.deliveringgoods.R
import com.my_project.deliveringgoods.data.provider.ResourceProvider
import com.my_project.deliveringgoods.ui.main.MainActivity
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule


class GeoService : Service() {

    companion object {
        var serviceRunning: Boolean = false
    }

    @Inject
    lateinit var rProvider: ResourceProvider
    @Inject
    lateinit var gpsTrekker: GpsTrekker

    private lateinit var notificationManager: NotificationManager
    private lateinit var timer: Timer
    private lateinit var handler: Handler

    override fun onCreate() {
        App.appComponent.inject(this)
        super.onCreate()
        serviceRunning = true
        handler = Handler()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            createChannel()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        timer = Timer(true)
        startSendGeoData()
        sendNotification()
        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceRunning = false
        gpsTrekker.clearCompositeDisposable()
        timer.cancel()
        gpsTrekker.stopTrekking()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.cancel(CONST.NOTIFY_ID)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(CONST.GEO_CHANNEL_ID, CONST.GEO_CHANNEL_NAME, importance)
        mChannel.apply {
            description = CONST.GEO_CHANNEL_DESCRIPTION
            setShowBadge(false)
            enableVibration(true)
            enableLights(true)
            lightColor = rProvider.getColor(R.color.colorPrimary)
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }
        notificationManager.createNotificationChannel(mChannel)
    }

    private fun startSendGeoData() {
        timer.schedule(CONST.TIMER_DELAY, CONST.SERVICE_TIMEOUT) {
            handler.post {
                gpsTrekker.startTrekking()
                gpsTrekker.startLocationManager()
            }
        }
    }

    private fun sendNotification() {
        val notificationIntent = Intent(this, MainActivity::class.java)
        notificationIntent.action = Intent.ACTION_MAIN
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val contentIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        val notify: NotificationCompat.Builder = if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(applicationContext, CONST.GEO_CHANNEL_ID)
        } else {
            NotificationCompat.Builder(applicationContext)
        }

        var defaults = 0
        defaults = defaults or Notification.DEFAULT_VIBRATE
        defaults = defaults or Notification.DEFAULT_SOUND
        defaults = defaults or Notification.DEFAULT_LIGHTS

        notify.setContentIntent(contentIntent)
            .apply {
                setOngoing(true)
                setDefaults(defaults)
                setSmallIcon(R.drawable.ic_gps_fixed_black_24dp)
                setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_gps_large_60dp))
                setTicker(rProvider.getString(R.string.enable_sending_of_coordinates))
                setContentTitle(rProvider.getString(R.string.started_sending_coordinates))
                setContentText(rProvider.getString(R.string.click_go_application))
                setWhen(System.currentTimeMillis())
            }
        startForeground(CONST.NOTIFY_ID, notify.build())
        Timber.d("GeoService---sendNotification()")
    }
}