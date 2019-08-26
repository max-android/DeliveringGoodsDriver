package com.my_project.deliveringgoods.di

import com.my_project.deliveringgoods.router.Router
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RouterModule {
    @Provides
    @Singleton
    fun provideRouter() = Router()
}