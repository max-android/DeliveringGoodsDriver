package com.my_project.deliveringgoods.viewmodels.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.my_project.deliveringgoods.App
import com.my_project.deliveringgoods.data.repository.MainRepository
import com.my_project.deliveringgoods.router.Router
import com.my_project.deliveringgoods.router.Screen
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class MainViewModel : ViewModel() {
    @Inject
    lateinit var mRepository: MainRepository
    @Inject
    lateinit var router: Router

    val mLiveData = MutableLiveData<MainViewState>()
    private val compositeDisposable = CompositeDisposable()

    init {
        App.appComponent.inject(this)
    }

    fun showHome() = router.replace(Screen.HOME)

    fun showListTasks() = router.replace(Screen.LIST_TASKS)

    fun showMap() = router.replace(Screen.MAP)

    fun showSettings() = router.replace(Screen.SETTINGS)

    fun showProfile() = router.replace(Screen.PROFILE)

    fun showInfo() = router.forward(Screen.INFO)

    fun back() = router.back()
}