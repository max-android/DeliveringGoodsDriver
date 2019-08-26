package com.my_project.deliveringgoods.di

import com.my_project.deliveringgoods.data.file_storage.ExternalStorage
import com.my_project.deliveringgoods.data.file_storage.InternalStorage
import com.my_project.deliveringgoods.data.local_storage.FirebaseHolder
import com.my_project.deliveringgoods.data.local_storage.UserDataHolder
import com.my_project.deliveringgoods.data.network.ApiService
import com.my_project.deliveringgoods.data.network.GeocoderManager
import com.my_project.deliveringgoods.data.provider.AppInfoProvider
import com.my_project.deliveringgoods.data.repository.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideLoginRepository(api: ApiService, dataHolder: UserDataHolder) = LoginRepository(api,dataHolder)

    @Provides
    @Singleton
    fun provideRegisterRepository(api: ApiService, dataHolder: UserDataHolder) = RegisterRepository(api,dataHolder)

    @Provides
    @Singleton
    fun provideSplashRepository(dataHolder: UserDataHolder) = SplashRepository(dataHolder)

    @Provides
    @Singleton
    fun provideInfoRepository(api: ApiService) = InfoRepository(api)

    @Provides
    @Singleton
    fun provideListTaskRepository(api: ApiService) = ListOrdersRepository(api)

    @Provides
    @Singleton
    fun provideMainRepository(api: ApiService) = MainRepository(api)

    @Provides
    @Singleton
    fun provideDeliveryMapRepository(api: ApiService,geocoder: GeocoderManager) = DeliveryMapRepository(api,geocoder)

    @Provides
    @Singleton
    fun provideProfileRepository(api: ApiService,dataHolder: UserDataHolder,storage:InternalStorage) = ProfileRepository(api,dataHolder,storage)

    @Provides
    @Singleton
    fun providerWorkerRepository(api: ApiService,dataHolder: UserDataHolder) = WorkerRepository(api,dataHolder)

    @Provides
    @Singleton
    fun provideSearchRepository(api: ApiService) = SearchRepository(api)

    @Provides
    @Singleton
    fun provideSettingsRepository(api: ApiService,holder: FirebaseHolder) = SettingsRepository(api,holder)

    @Provides
    @Singleton
    fun provideTaskRepository(api: ApiService) = OrderRepository(api)

    @Provides
    @Singleton
    fun provideHomeRepository(api: ApiService) = HomeRepository(api)

    @Provides
    @Singleton
    fun provideJobRepository(api: ApiService) = JobRepository(api)

    @Provides
    @Singleton
    fun provideGpsRepository(api: ApiService) =  GpsRepository(api)

    @Provides
    @Singleton
    fun providePhotoRepository(api: ApiService,storage: ExternalStorage) =  PhotoRepository(api,storage)

    @Provides
    @Singleton
    fun provideFirebaseRepository(api: ApiService, appInfoProvider: AppInfoProvider, holder: FirebaseHolder) = FirebaseRepository(api,appInfoProvider,holder)

}