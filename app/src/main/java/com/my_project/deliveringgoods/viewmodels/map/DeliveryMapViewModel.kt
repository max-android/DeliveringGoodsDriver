package com.my_project.deliveringgoods.viewmodels.map

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.maps.model.DirectionsRoute
import com.my_project.deliveringgoods.App
import com.my_project.deliveringgoods.data.repository.DeliveryMapRepository
import com.my_project.deliveringgoods.router.Router
import com.my_project.deliveringgoods.router.Screen
import com.my_project.deliveringgoods.utilities.getMillis
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject


class DeliveryMapViewModel : ViewModel() {

    @Inject
    lateinit var dmRepository: DeliveryMapRepository
    @Inject
    lateinit var router: Router
    val dmLiveData = MutableLiveData<DeliveryMapViewState>()
    var vmMyLatLng: LatLng? = null
    var vmDestinationLatLng: LatLng? = null
    var vmSelectRoute: DirectionsRoute? = null
    private val compositeDisposable = CompositeDisposable()

    init {
        App.appComponent.inject(this)
    }

    private val exceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        dmLiveData.value = DeliveryMapViewState.Error(throwable)
    }

    fun search(search: String) = router.forward(Screen.SEARCH, search)


    @SuppressLint("CheckResult")
    fun addressDestinationRequest(position: LatLng) {
        dmRepository.requestAddress(position)
            .subscribe(
                { dmLiveData.value = DeliveryMapViewState.SuccessAddress(it[0]) },
                { dmLiveData.value = DeliveryMapViewState.Error(it) }
            ).addTo(compositeDisposable)

    }

    @SuppressLint("CheckResult")
    fun routeRequest(
        positionFrom: com.google.maps.model.LatLng,
        positionTo: com.google.maps.model.LatLng,
        vararg waypoints: com.google.maps.model.LatLng
    ) {
        dmRepository.route(positionFrom, positionTo, waypoints.map { it })
            .doOnSubscribe { startProgress() }
            .subscribe(
                { dmLiveData.value = DeliveryMapViewState.SuccessRoute(it) },
                { dmLiveData.value = DeliveryMapViewState.Error(it) }
            ).addTo(compositeDisposable)
    }


    fun ordersRequest() {
        startProgress()
        viewModelScope.launch(exceptionHandler) {
            val response = withContext(Dispatchers.IO) { dmRepository.orders() }
            if (response.isSuccessful) {
                dmLiveData.value = response.body()?.let { orders ->
                    DeliveryMapViewState.SuccessOrders(orders.sortedBy { it.startTime.getMillis })
                }
            } else {
                Timber.e(response.errorBody()?.string())
            }
        }
    }

    private fun startProgress() {
        dmLiveData.value = DeliveryMapViewState.Loading()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}