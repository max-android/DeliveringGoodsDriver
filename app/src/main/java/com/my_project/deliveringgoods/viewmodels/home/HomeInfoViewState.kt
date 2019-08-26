package com.my_project.deliveringgoods.viewmodels.home

import com.my_project.deliveringgoods.data.entities.AdvInfo

sealed class HomeInfoViewState {
    class SuccessInfo(val info: List<AdvInfo>): HomeInfoViewState()
    class Loading: HomeInfoViewState()
    class Error(val error: Throwable): HomeInfoViewState()
}