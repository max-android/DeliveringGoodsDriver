package com.my_project.deliveringgoods.viewmodels.register

import com.my_project.deliveringgoods.data.entities.RegistrationResponse


sealed class RegisterViewState {
    class SuccessRegistration(val rResponse: RegistrationResponse): RegisterViewState()
    class Loading:RegisterViewState()
    class Error(val error: Throwable): RegisterViewState()
}