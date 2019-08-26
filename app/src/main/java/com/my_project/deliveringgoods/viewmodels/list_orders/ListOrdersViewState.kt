package com.my_project.deliveringgoods.viewmodels.list_orders

import com.my_project.deliveringgoods.data.entities.Order

sealed class ListOrdersViewState {
    class SuccessOrders(val orders: List<Order>): ListOrdersViewState()
    class Loading:ListOrdersViewState()
    class Error(val error: Throwable): ListOrdersViewState()
}