package com.my_project.deliveringgoods.viewmodels.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.my_project.deliveringgoods.App
import com.my_project.deliveringgoods.data.repository.SettingsRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class SettingsViewModel : ViewModel() {

    @Inject
    lateinit var sRepository: SettingsRepository
    val sLiveData = MutableLiveData<SettingsViewState>()
    private val compositeDisposable = CompositeDisposable()

    init {
        App.appComponent.inject(this)
    }

    fun enabledPushRequest() {
        sLiveData.value = SettingsViewState.SuccessEnabledFirebaseService(sRepository.enabledPush().enabledPush)
    }

    fun setEnabledPush(enabledPush: Boolean) {
        sRepository.enabledPush().enabledPush = enabledPush
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}