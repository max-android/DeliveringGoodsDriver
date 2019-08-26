package com.my_project.deliveringgoods.di

import android.content.Context
import com.my_project.deliveringgoods.data.local_storage.FirebaseHolder

import com.my_project.deliveringgoods.data.local_storage.UserDataHolder
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class DataHolderModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideUserDataHolder() = UserDataHolder(context)

    @Provides
    @Singleton
    fun provideFirebaseTokenHolder() = FirebaseHolder(context)
}