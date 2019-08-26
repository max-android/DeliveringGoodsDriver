package com.my_project.deliveringgoods.ui.services

import CONST
import addClearStackFlags
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import androidx.core.app.NotificationCompat

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.my_project.deliveringgoods.App
import com.my_project.deliveringgoods.R
import com.my_project.deliveringgoods.data.entities.ResponseResult
import com.my_project.deliveringgoods.data.repository.FirebaseRepository
import com.my_project.deliveringgoods.ui.main.MainActivity
import com.my_project.deliveringgoods.utilities.bitmapFromVectorDrawable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject


class DGFirebaseService : FirebaseMessagingService() {

    @Inject
    lateinit var firebaseRepository: FirebaseRepository

    init {
        App.appComponent.inject(this)
    }

    override fun onNewToken(p0: String) {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
            if (!it.isSuccessful) {
                return@addOnCompleteListener
            }
            val token = it.result?.token
            token?.let {
                sendTokenToServer(it)
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val map = message.data
        val body = map["body"]
        val title = map["title"]
        if (body != null && title != null) {
            sendNotification(body, title)
        }
    }

    private fun sendTokenToServer(token: String) {
        firebaseRepository.token().token = token
        firebaseRepository.updatePushToken().enqueue(object : Callback<ResponseResult> {
            override fun onFailure(call: Call<ResponseResult>, t: Throwable) {
                Timber.e(t)
            }

            override fun onResponse(call: Call<ResponseResult>, response: Response<ResponseResult>) {
                if (response.isSuccessful)
                    Timber.tag("--FirebaseService").d("sendTokenToServer-Successful")
                else Timber.tag("--FirebaseService").d("sendTokenToServer- not Successful")
            }
        })
    }

    private fun sendNotification(body: String, title: String) {
        val intent = MainActivity.newInstance(applicationContext).addClearStackFlags()
        val pi = PendingIntent.getActivity(
            applicationContext,
            CONST.FIREBASE_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val nm = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val audioAttributes = AudioAttributes.Builder().apply {
                setUsage(AudioAttributes.USAGE_NOTIFICATION)
                setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
            }.build()
            val channel: NotificationChannel = NotificationChannel(
                CONST.ID_FIREBASE_CHANNEL,
                CONST.FIREBASE_CHANNEL,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.apply {
                enableVibration(true)
                enableLights(true)
                channel.setSound(Uri.parse("android.resource://" + packageName + "/" + R.raw.notify), audioAttributes)
            }
            nm.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(
            applicationContext, CONST.ID_FIREBASE_CHANNEL
        ).apply {
            setContentTitle(title)
            setAutoCancel(true)
            setLargeIcon(bitmapFromVectorDrawable(R.drawable.ic_event_available))
            setSmallIcon(R.drawable.ic_event_available)
            setContentText(body)
            setContentIntent(pi)
            priority = NotificationCompat.PRIORITY_HIGH
            setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        }
        nm.notify(CONST.FIREBASE_REQUEST_CODE, builder.build())
    }
}