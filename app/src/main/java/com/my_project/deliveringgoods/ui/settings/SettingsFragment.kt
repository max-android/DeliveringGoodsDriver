package com.my_project.deliveringgoods.ui.settings

import CONST
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.messaging.FirebaseMessaging
import com.my_project.deliveringgoods.App
import com.my_project.deliveringgoods.R
import com.my_project.deliveringgoods.data.provider.ResourceProvider
import com.my_project.deliveringgoods.ui.base.BaseFragment
import com.my_project.deliveringgoods.ui.services.GeoService
import com.my_project.deliveringgoods.ui.services.InfoJobService
import com.my_project.deliveringgoods.utilities.LocationPermission
import com.my_project.deliveringgoods.viewmodels.settings.SettingsViewModel
import com.my_project.deliveringgoods.viewmodels.settings.SettingsViewState
import gone
import kotlinx.android.synthetic.main.fragment_settings.*
import mainActivity
import org.jetbrains.anko.design.snackbar
import status
import javax.inject.Inject

class SettingsFragment : BaseFragment() {

    companion object {
        private const val SETTINGS_KEY = "settings_key"

        @JvmStatic
        fun newInstance(): Fragment = SettingsFragment()
    }

    @Inject
    lateinit var rProvider: ResourceProvider
    private lateinit var viewModel: SettingsViewModel
    private lateinit var intentService: Intent

    override fun getLayoutRes(): Int = R.layout.fragment_settings

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        App.appComponent.inject(this)
        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
        askPermissions()
        init()
        enablePushRequest()
        observeData()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == LocationPermission.LOCATION_PERMISSION_REQUEST_CODE)
            if (grantResults.size == 2
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                view!!.snackbar(R.string.permission_location_granted)
                Handler().postDelayed({ switchOnline.isEnabled = true }, CONST.GEO_DELAY)
            } else {
                view!!.snackbar(R.string.permission_location_denied)
            }
    }

    private fun init() {
        updateToolbar()
        intentService = Intent(context, GeoService::class.java)
        switchOnline.status(GeoService.serviceRunning)
        switchInfo.status(InfoJobService.infoRunning)
        setListener()
        if (Build.VERSION.SDK_INT < 23) switchOnline.isEnabled = true
    }

    private fun enablePushRequest() = viewModel.enabledPushRequest()

    private fun updateToolbar() {
        mainActivity?.toolbar?.gone()
    }

    private fun setListener() {
        switchOnline.setOnClickListener { startGeoService() }
        switchInfo.setOnClickListener { startInfoService() }
        pushInfo.setOnCheckedChangeListener { _, checked -> updateFirebaseService(checked) }
    }

    private fun askPermissions() {
        LocationPermission().requestPermission(context!!) {
            switchOnline.isEnabled = true
        }
    }

    private fun observeData() = viewModel.sLiveData.observe(this, Observer { processViewState(it) })

    private fun processViewState(viewState: SettingsViewState?) {
        viewState?.let {
            when (it) {
                is SettingsViewState.SuccessEnabledFirebaseService -> updateUi(it.enabled)
            }
        }
    }

    private fun updateFirebaseService(enabledPush: Boolean) {
        subscribePushMessageService(enabledPush)
        viewModel.setEnabledPush(enabledPush)
    }

    private fun updateUi(enabledPush: Boolean) {
        pushInfo.isChecked = enabledPush
    }

    private fun subscribePushMessageService(enabledPush: Boolean) {
        if (enabledPush) FirebaseMessaging.getInstance().subscribeToTopic("android")
        else FirebaseMessaging.getInstance().unsubscribeFromTopic("android")
    }

    private fun startGeoService() {
        if (!GeoService.serviceRunning) {
            switchOnline.isChecked = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) activity?.startForegroundService(intentService)
            else activity?.startService(intentService)
        } else {
            switchOnline.isChecked = false
            activity?.stopService(intentService)
        }
    }

    private fun startInfoService() {
        val jobScheduler = context?.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        if (!InfoJobService.infoRunning) {
            switchInfo.isChecked = true
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val jobInfo = JobInfo.Builder(CONST.INFO_JOB_ID, ComponentName(context!!, InfoJobService::class.java))
                    .apply {
                        setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        setOverrideDeadline(CONST.INFO_SERVICE_DEADLINE)
                        setPersisted(true)
                    }.build()
                jobScheduler.schedule(jobInfo)
            } else {
                val jobInfo = JobInfo.Builder(CONST.INFO_JOB_ID, ComponentName(context!!, InfoJobService::class.java))
                    .apply {
                        // .setPeriodic(Constants.INFO_PERIODIC_REQUEST)
                        setOverrideDeadline(CONST.INFO_SERVICE_DEADLINE)
                        setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        setPersisted(true)
                    }.build()
                jobScheduler.schedule(jobInfo)
            }
        } else {
            switchInfo.isChecked = false
            jobScheduler.cancel(CONST.INFO_JOB_ID)
        }
    }

}