package com.my_project.deliveringgoods.viewmodels.profile


sealed class ProfileViewState {
    class SuccessLogout(val result: Boolean): ProfileViewState()
    class SuccessSaveProfileImage(val save: Boolean): ProfileViewState()
    class SuccessProfileImage(val url: String): ProfileViewState()
    class Loading: ProfileViewState()
    class Error(val error: Throwable): ProfileViewState()
}