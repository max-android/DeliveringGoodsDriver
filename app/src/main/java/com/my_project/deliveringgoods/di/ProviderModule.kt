package com.my_project.deliveringgoods.di

import android.content.Context
import com.my_project.deliveringgoods.data.provider.AppInfoProvider
import com.my_project.deliveringgoods.data.provider.ResourceProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class ProviderModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideAppInfoProvider() = AppInfoProvider(context)

    @Provides
    @Singleton
    fun provideResourceProvider() = ResourceProvider(context)
}