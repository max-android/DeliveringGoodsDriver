package com.my_project.deliveringgoods.viewmodels.order


sealed class OrderViewState {
    class SuccessAcceptOrder(val result: Boolean) : OrderViewState()
    class Loading : OrderViewState()
    class Error(val error: Throwable) : OrderViewState()
}
