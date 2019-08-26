package com.my_project.deliveringgoods

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.firebase.FirebaseApp
import com.jakewharton.threetenabp.AndroidThreeTen
import com.my_project.deliveringgoods.di.*
import timber.log.Timber


class App : Application() {

    companion object {
        @JvmStatic
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .routerModule(RouterModule())
            .networkModule(NetworkModule(this))
            .dataHolderModule(DataHolderModule(this))
            .repositoryModule(RepositoryModule())
            .providerModule(ProviderModule(this))
            .fileModule(FileModule(this))
            .gpsModule(GpsModule(this))
            .build()

        initAndroidThreeTen()
        initFresco()
        initTimber()
        initFirebase()
    }

    private fun initAndroidThreeTen() = AndroidThreeTen.init(this)

    private fun initFresco() = Fresco.initialize(this)

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initFirebase() {
        FirebaseApp.initializeApp(this)
    }
}
