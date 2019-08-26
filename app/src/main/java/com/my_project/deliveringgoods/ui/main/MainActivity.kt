package com.my_project.deliveringgoods.ui.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.aurelhubert.ahbottomnavigation.notification.AHNotification
import com.my_project.deliveringgoods.App
import com.my_project.deliveringgoods.R
import com.my_project.deliveringgoods.data.provider.ResourceProvider
import com.my_project.deliveringgoods.router.Router
import com.my_project.deliveringgoods.router.Screen
import com.my_project.deliveringgoods.ui.base.BaseActivity
import com.my_project.deliveringgoods.ui.map.DeliveryMapFragment
import com.my_project.deliveringgoods.ui.photo.PhotoFragment
import com.my_project.deliveringgoods.ui.profile.ProfileFragment
import com.my_project.deliveringgoods.ui.settings.SettingsFragment
import com.my_project.deliveringgoods.utilities.CameraPermission
import com.my_project.deliveringgoods.utilities.LocationPermission
import com.my_project.deliveringgoods.utilities.WritePermission
import com.my_project.deliveringgoods.viewmodels.main.MainViewModel
import visible
import javax.inject.Inject

class MainActivity : BaseActivity() {

    companion object {
        private const val TAB_HOME = 0
        private const val TAB_TASKS = 1
        private const val TAB_MAP = 2
        private const val TAB_SETTINGS = 3
        private const val TAB_PROFILE = 4
        fun newInstance(context: Context) = Intent(context, MainActivity::class.java)
    }

    @Inject
    lateinit var rProvider: ResourceProvider
    @Inject
    lateinit var router: Router
    private lateinit var viewModel: MainViewModel
    private var infoMenuItem: MenuItem? = null
    internal lateinit var toolbar: Toolbar
    lateinit var bottomNavigation: AHBottomNavigation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.appComponent.inject(this)
        router.init(supportFragmentManager)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        if (savedInstanceState == null) {
            viewModel.showHome()
        }
        init()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        infoMenuItem = menu?.findItem(R.id.action_info)
        if (router.actualScreen == Screen.HOME) changeMenuToolBar(true) else changeMenuToolBar(false)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item!!.itemId) {
        R.id.action_info -> {
            viewModel.showInfo()
            true
        }
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() = when (router.actualScreen) {
        Screen.HOME -> super.onBackPressed()
        Screen.LIST_TASKS -> super.onBackPressed()
        Screen.MAP -> super.onBackPressed()
        Screen.SETTINGS -> super.onBackPressed()
        Screen.PROFILE -> super.onBackPressed()
        Screen.INFO -> viewModel.back()
        Screen.SEARCH -> viewModel.back()
        Screen.TASK -> viewModel.back()
        Screen.PHOTO -> viewModel.back()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            WritePermission.WRITE_FILE_PERMISSION_REQUEST_CODE -> {
                val profileFragment = (supportFragmentManager.findFragmentById(R.id.main_container) as? ProfileFragment)
                profileFragment?.requestPermissions(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    WritePermission.WRITE_FILE_PERMISSION_REQUEST_CODE
                )
            }

            CameraPermission.CAMERA_PERMISSION_REQUEST_CODE -> {
                val profileFragment = (supportFragmentManager.findFragmentById(R.id.main_container) as? PhotoFragment)
                profileFragment?.requestPermissions(
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    CameraPermission.CAMERA_PERMISSION_REQUEST_CODE
                )

            }

            LocationPermission.LOCATION_PERMISSION_REQUEST_CODE -> {
                if (router.actualScreen == Screen.MAP) {
                    val mapFragment =
                        (supportFragmentManager.findFragmentById(R.id.main_container) as? DeliveryMapFragment)
                    mapFragment?.requestPermissions(
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                        LocationPermission.LOCATION_PERMISSION_REQUEST_CODE
                    )
                }

                if (router.actualScreen == Screen.SETTINGS) {
                    val settingsFragment =
                        (supportFragmentManager.findFragmentById(R.id.main_container) as? SettingsFragment)
                    settingsFragment?.requestPermissions(
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                        LocationPermission.LOCATION_PERMISSION_REQUEST_CODE
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        router.detach()
        super.onDestroy()
    }

    private fun init() {
        initToolbar()
        initBottomNavigation()
    }

    private fun initToolbar() {
        toolbar = findViewById(R.id.toolBar)
        setSupportActionBar(toolbar)
    }

    private fun initBottomNavigation() {
        bottomNavigation = findViewById(R.id.bottomNavigation)
        bottomNavigation.apply {
            titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW
            setTitleTextSizeInSp(
                rProvider.getFloat(R.dimen.bottom_bar_text),
                rProvider.getFloat(R.dimen.bottom_bar_text)
            )
            accentColor = rProvider.getColor(R.color.colorPrimary)
            inactiveColor = rProvider.getColor(R.color.darker_gray)

            addItems(
                listOf(
                    AHBottomNavigationItem(
                        rProvider.getString(R.string.home),
                        rProvider.getDrawable(R.drawable.ic_home)
                    ),
                    AHBottomNavigationItem(
                        rProvider.getString(R.string.tasks),
                        rProvider.getDrawable(R.drawable.ic_tasks)
                    ), AHBottomNavigationItem(
                        rProvider.getString(R.string.map),
                        rProvider.getDrawable(R.drawable.ic_map)
                    ), AHBottomNavigationItem(
                        rProvider.getString(R.string.settings),
                        rProvider.getDrawable(R.drawable.ic_settings)
                    ), AHBottomNavigationItem(
                        rProvider.getString(R.string.profile),
                        rProvider.getDrawable(R.drawable.ic_profile)
                    )
                )
            )

            setNotification(AHNotification.Builder().apply {
                setBackgroundColor(rProvider.getColor(R.color.red))
                setTextColor(rProvider.getColor(R.color.white))
            }.build(), TAB_TASKS)

            setOnTabSelectedListener { position, wasSelected -> setBottomNavigationListener(position, wasSelected) }
        }
    }

    private fun setBottomNavigationListener(position: Int, wasSelected: Boolean) = run {
        if (!wasSelected) {
            when (position) {
                TAB_HOME -> {
                    viewModel.showHome()
                }
                TAB_TASKS -> {
                    viewModel.showListTasks()
                }
                TAB_MAP -> {
                    viewModel.showMap()
                }
                TAB_SETTINGS -> {
                    viewModel.showSettings()
                }
                TAB_PROFILE -> {
                    viewModel.showProfile()
                }
            }
        }
        true
    }

    fun updateToolbar(titleToolbar: String, arrow: Boolean) {
        toolbar.visible()
        supportActionBar?.title = titleToolbar
        if (arrow) supportActionBar?.setDisplayHomeAsUpEnabled(true)
        else supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    fun changeMenuToolBar(visible: Boolean) {
        infoMenuItem?.isVisible = visible
    }

}
