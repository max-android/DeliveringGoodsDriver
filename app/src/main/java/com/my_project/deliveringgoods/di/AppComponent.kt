package com.my_project.deliveringgoods.di

import com.my_project.deliveringgoods.ui.home.HomeFragment
import com.my_project.deliveringgoods.ui.info.InfoFragment
import com.my_project.deliveringgoods.ui.list_orders.ListOrdersFragment
import com.my_project.deliveringgoods.ui.login.LoginActivity
import com.my_project.deliveringgoods.ui.main.MainActivity
import com.my_project.deliveringgoods.ui.map.DeliveryMapFragment
import com.my_project.deliveringgoods.ui.order.OrderFragment
import com.my_project.deliveringgoods.ui.photo.PhotoFragment
import com.my_project.deliveringgoods.ui.profile.ProfileFragment
import com.my_project.deliveringgoods.ui.register.RegisterActivity
import com.my_project.deliveringgoods.ui.search.SearchFragment
import com.my_project.deliveringgoods.ui.services.DGFirebaseService
import com.my_project.deliveringgoods.ui.services.GeoService
import com.my_project.deliveringgoods.ui.services.InfoJobService
import com.my_project.deliveringgoods.ui.services.TokenWorker
import com.my_project.deliveringgoods.ui.settings.SettingsFragment
import com.my_project.deliveringgoods.viewmodels.home.HomeViewModel
import com.my_project.deliveringgoods.viewmodels.info.InfoViewModel
import com.my_project.deliveringgoods.viewmodels.list_orders.ListOrdersViewModel
import com.my_project.deliveringgoods.viewmodels.login.LoginViewModel
import com.my_project.deliveringgoods.viewmodels.main.MainViewModel
import com.my_project.deliveringgoods.viewmodels.map.DeliveryMapViewModel
import com.my_project.deliveringgoods.viewmodels.order.OrderViewModel
import com.my_project.deliveringgoods.viewmodels.photo.PhotoViewModel
import com.my_project.deliveringgoods.viewmodels.profile.ProfileViewModel
import com.my_project.deliveringgoods.viewmodels.register.RegisterViewModel
import com.my_project.deliveringgoods.viewmodels.search.SearchViewModel
import com.my_project.deliveringgoods.viewmodels.settings.SettingsViewModel
import com.my_project.deliveringgoods.viewmodels.splash.SplashViewModel
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component
    (
    modules = [
        NetworkModule::class,
        RepositoryModule::class,
        DataHolderModule::class,
        ProviderModule::class,
        RouterModule::class,
        FileModule::class,
        GpsModule::class
    ]
)
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(loginActivity: LoginActivity)
    fun inject(registerActivity: RegisterActivity)
    fun inject(geoService: GeoService)
    fun inject(infoJobService: InfoJobService)
    fun inject(firebaseService: DGFirebaseService)
    fun inject(tokenWorker: TokenWorker)
    fun inject(homeFragment: HomeFragment)
    fun inject(infoFragment: InfoFragment)
    fun inject(listOrdersFragment: ListOrdersFragment)
    fun inject(deliveryMapFragment: DeliveryMapFragment)
    fun inject(profileFragment: ProfileFragment)
    fun inject(searchFragment: SearchFragment)
    fun inject(settingsFragment: SettingsFragment)
    fun inject(orderFragment: OrderFragment)
    fun inject(photoFragment: PhotoFragment)
    fun inject(loginViewModel: LoginViewModel)
    fun inject(registerViewModel: RegisterViewModel)
    fun inject(splashViewModel: SplashViewModel)
    fun inject(infoViewModel: InfoViewModel)
    fun inject(listOrdersViewModel: ListOrdersViewModel)
    fun inject(mainViewModel: MainViewModel)
    fun inject(deliveryMapViewModel: DeliveryMapViewModel)
    fun inject(profileViewModel: ProfileViewModel)
    fun inject(searchViewModel: SearchViewModel)
    fun inject(settingsViewModel: SettingsViewModel)
    fun inject(orderViewModel: OrderViewModel)
    fun inject(homeViewModel: HomeViewModel)
    fun inject(photoViewModel: PhotoViewModel)
}