package com.my_project.deliveringgoods.viewmodels.settings


sealed class SettingsViewState {
    class SuccessEnabledFirebaseService(val enabled: Boolean): SettingsViewState()
}