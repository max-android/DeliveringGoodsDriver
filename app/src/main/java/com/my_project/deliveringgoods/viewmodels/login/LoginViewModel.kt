package com.my_project.deliveringgoods.viewmodels.login

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.my_project.deliveringgoods.App
import com.my_project.deliveringgoods.data.repository.LoginRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject


class LoginViewModel : ViewModel() {

    @Inject
    lateinit var lRepository: LoginRepository
    val loginLiveData = MutableLiveData<LoginViewState>()
    private val compositeDisposable = CompositeDisposable()

    init {
        App.appComponent.inject(this)
    }

    @SuppressLint("CheckResult")
    fun login(username: String, password: String) {
        lRepository.login(username, password)
            .doOnSuccess { lRepository.user().token = it.token }
            .doOnSubscribe { startProgress() }
            .subscribe(
                { registration -> loginLiveData.value = LoginViewState.SuccessLogin(registration) },
                { error -> loginLiveData.value = LoginViewState.Error(error) }
            ).addTo(compositeDisposable)
    }

    private fun startProgress() {
        loginLiveData.value = LoginViewState.Loading()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}