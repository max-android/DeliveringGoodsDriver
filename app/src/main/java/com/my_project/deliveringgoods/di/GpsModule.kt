package com.my_project.deliveringgoods.di

import android.content.Context
import com.my_project.deliveringgoods.data.repository.GpsRepository
import com.my_project.deliveringgoods.ui.services.GpsTrekker

import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class GpsModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideGpsTrekker(gpsRepository: GpsRepository) = GpsTrekker(context, gpsRepository)
}