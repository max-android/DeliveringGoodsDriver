package com.my_project.deliveringgoods.viewmodels.list_orders

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my_project.deliveringgoods.App
import com.my_project.deliveringgoods.data.entities.Order
import com.my_project.deliveringgoods.data.repository.ListOrdersRepository
import com.my_project.deliveringgoods.router.Router
import com.my_project.deliveringgoods.router.Screen
import com.my_project.deliveringgoods.utilities.getMillis
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class ListOrdersViewModel : ViewModel() {
    @Inject
    lateinit var loRepository: ListOrdersRepository
    @Inject
    lateinit var router: Router
    val loLiveData = MutableLiveData<ListOrdersViewState>()

    private val exceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        loLiveData.value = ListOrdersViewState.Error(throwable)
    }

    init {
        App.appComponent.inject(this)
    }

    fun ordersRequest() {
        startProgress()
        viewModelScope.launch(exceptionHandler) {
            val response = withContext(Dispatchers.IO) { loRepository.orders() }
            if (response.isSuccessful) {
                loLiveData.value = response.body()?.let { orders ->
                    ListOrdersViewState.SuccessOrders(orders.sortedBy { it.startTime.getMillis })
                }
            } else {
                Timber.e(response.errorBody()?.string())
            }
        }
    }

    fun onClickItem(order: Order) = router.forward(Screen.TASK, order)

    private fun startProgress() {
        loLiveData.value = ListOrdersViewState.Loading()
    }
}