package com.my_project.deliveringgoods.viewmodels.splash


sealed class SplashViewState {
    class SuccessExistUser(val status:Boolean):SplashViewState()
}