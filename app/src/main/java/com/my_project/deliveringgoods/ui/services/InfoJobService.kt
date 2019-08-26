package com.my_project.deliveringgoods.ui.services

import CONST
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.my_project.deliveringgoods.App
import com.my_project.deliveringgoods.R
import com.my_project.deliveringgoods.data.provider.ResourceProvider
import com.my_project.deliveringgoods.data.repository.JobRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import timber.log.Timber
import java.util.*
import javax.inject.Inject


class InfoJobService : JobService() {

    companion object {
        var infoRunning: Boolean = false
    }

    @Inject
    lateinit var jobRepository: JobRepository
    @Inject
    lateinit var rProvider: ResourceProvider
    private lateinit var notificationManager: NotificationManager
    private val compositeDisposable = CompositeDisposable()
    private var i = 0

    init {
        App.appComponent.inject(this)
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
        Timber.d("InfoJobService---onCreate")
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        infoRunning = true
        i++
        Timber.d("InfoJobService---onStartJob-$i")
        jobRepository.sendInfoRequest(CONST.INFO_CODE)
            .subscribe(
                { info -> showInfo(info.message, info.contentInfo) },
                { error -> Timber.e(error) }
            ).addTo(compositeDisposable)

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            jobFinished(params, true)
        }
        /*
		"false" - говорим о том, что никакая работа больше не выполняется, если в методе onStartJob() делегируется
		выполнение задачи в другие потоки, то необходимо вернуть "true".
		 */
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        infoRunning = false
        compositeDisposable.clear()
        Timber.d("InfoJobService---onStopJob")
        /*
		"false" - прерванная задача считается выполненной и будет удалена из очереди (если она не была периодической);
		"true" - JobScheduler поставит прерванную задачу в очередь выполнения снова.
		 */
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        notificationManager.cancelAll()
        Timber.d("InfoJobService---onDestroy()")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(CONST.INFO_CHANNEL_ID, CONST.INFO_CHANNEL_NAME, importance)
        mChannel.description = CONST.INFO_CHANNEL_DESCRIPTION
        mChannel.setShowBadge(false)
        mChannel.enableVibration(true)
        mChannel.enableLights(true)
        mChannel.lightColor = rProvider.getColor(R.color.colorPrimary)
        mChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        notificationManager.createNotificationChannel(mChannel)
        Timber.d("InfoService---createChannel()")
    }

    private fun showInfo(message: String, contentInfo: String) {
        val notify: NotificationCompat.Builder = if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(applicationContext, CONST.INFO_CHANNEL_ID)
        } else {
            NotificationCompat.Builder(applicationContext)
        }
        Timber.d("InfoJobService---showInfo")
        var defaults = 0
        defaults = defaults or Notification.DEFAULT_VIBRATE
        defaults = defaults or Notification.DEFAULT_SOUND
        defaults = defaults or Notification.DEFAULT_LIGHTS

        notify.setSmallIcon(android.R.drawable.ic_dialog_email)
            .setAutoCancel(true)
            .setContentInfo(contentInfo)
            .setColor(rProvider.getColor(R.color.colorPrimary))
            .setShowWhen(true)
            .setUsesChronometer(true)
            .setDefaults(defaults)
            .setContentTitle(rProvider.getString(R.string.code_info))
            .setContentText(message)
        notificationManager.notify(Random().nextInt(), notify.build())
    }
}