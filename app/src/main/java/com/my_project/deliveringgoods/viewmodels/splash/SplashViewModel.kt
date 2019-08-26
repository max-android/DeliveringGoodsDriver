package com.my_project.deliveringgoods.viewmodels.splash

import CONST.DEFAULT_TOKEN
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.my_project.deliveringgoods.App
import com.my_project.deliveringgoods.data.repository.SplashRepository

import javax.inject.Inject


class SplashViewModel : ViewModel() {

    @Inject
    lateinit var sRepository: SplashRepository
    val sLiveData = MutableLiveData<SplashViewState>()

    init {
        App.appComponent.inject(this)
    }

    fun user() {
        if (sRepository.user().token == DEFAULT_TOKEN) sLiveData.value = SplashViewState.SuccessExistUser(false)
        else sLiveData.value = SplashViewState.SuccessExistUser(true)
    }

}