package com.my_project.deliveringgoods.di

import android.content.Context
import com.my_project.deliveringgoods.data.file_storage.ExternalStorage
import com.my_project.deliveringgoods.data.file_storage.InternalStorage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class FileModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideExternalStorage() = ExternalStorage(context)

    @Provides
    @Singleton
    fun provideInternalStorage() = InternalStorage(context)
}