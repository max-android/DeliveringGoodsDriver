package com.my_project.deliveringgoods.viewmodels.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my_project.deliveringgoods.App
import com.my_project.deliveringgoods.data.repository.OrderRepository
import com.my_project.deliveringgoods.router.Router
import com.my_project.deliveringgoods.utilities.ApiConst
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject


class OrderViewModel : ViewModel() {

    @Inject
    lateinit var tRepository: OrderRepository
    val oLiveData = MutableLiveData<OrderViewState>()
    @Inject
    lateinit var router: Router

    private val exceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        oLiveData.value = OrderViewState.Error(throwable)
    }

    init {
        App.appComponent.inject(this)
    }

    fun acceptOrderRequest(relevantId: String) {
        startProgress()
        viewModelScope.launch(exceptionHandler) {
            val response = withContext(Dispatchers.IO) {
                tRepository.acceptOrder(relevantId, ApiConst.ACCEPT_ORDER)
            }
            if (response.isSuccessful) {
                response.body()?.let {
                    oLiveData.value = OrderViewState.SuccessAcceptOrder(it.response)
                }
            } else {
                Timber.e(response.errorBody()?.string())
            }
        }
    }


    fun back() = router.back()

    private fun startProgress() {
        oLiveData.value = OrderViewState.Loading()
    }
}