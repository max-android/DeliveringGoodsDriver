package com.my_project.deliveringgoods.viewmodels.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my_project.deliveringgoods.App
import com.my_project.deliveringgoods.data.repository.RegisterRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject


class RegisterViewModel : ViewModel() {

    @Inject
    lateinit var rRepository: RegisterRepository
    val registerLiveData = MutableLiveData<RegisterViewState>()

    private val exceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        registerLiveData.value = RegisterViewState.Error(throwable)
    }

    init {
        App.appComponent.inject(this)
    }

    fun register(username: String, mail: String, password: String) {
        startProgress()
        viewModelScope.launch(exceptionHandler) {
            val response = withContext(Dispatchers.IO) { rRepository.register(username, mail, password) }
            if (response.isSuccessful) {
                registerLiveData.value = response.body()?.let {
                    rRepository.user().token = it.token
                    RegisterViewState.SuccessRegistration(it)
                }
            } else {
                Timber.e(response.errorBody()?.string())
            }
        }
    }

    private fun startProgress() {
        registerLiveData.value = RegisterViewState.Loading()
    }
}