package com.my_project.deliveringgoods.viewmodels.map

import android.location.Address
import com.google.maps.model.DirectionsRoute
import com.my_project.deliveringgoods.data.entities.Order


sealed class DeliveryMapViewState {
    class Loading: DeliveryMapViewState()
    class SuccessAddress(val address:Address): DeliveryMapViewState()
    class SuccessRoute(val directionsRoute: DirectionsRoute): DeliveryMapViewState()
    class SuccessOrders(val orders: List<Order>): DeliveryMapViewState()
    class Error(val error: Throwable): DeliveryMapViewState()
}
