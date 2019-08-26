package com.my_project.deliveringgoods.viewmodels.login

import com.my_project.deliveringgoods.data.entities.RegistrationResponse

sealed class LoginViewState {
    class SuccessLogin(val rResponse: RegistrationResponse): LoginViewState()
    class Loading: LoginViewState()
    class Error(val error: Throwable): LoginViewState()
}