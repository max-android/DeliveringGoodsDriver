package com.my_project.deliveringgoods.viewmodels.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.my_project.deliveringgoods.App
import com.my_project.deliveringgoods.data.repository.HomeRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class HomeViewModel : ViewModel() {

    @Inject
    lateinit var hRepository: HomeRepository
    val infoLiveData = MutableLiveData<HomeInfoViewState>()
    val shiftsLiveData = MutableLiveData<HomeShiftViewState>()
    private val compositeDisposable = CompositeDisposable()

    init {
        App.appComponent.inject(this)
    }

    fun infoRequest() {
        hRepository.info()
            .doOnSubscribe { startProgress() }
            .subscribe(
                { info ->
                    infoLiveData.value =
                        HomeInfoViewState.SuccessInfo(info)
                },
                { error ->
                    infoLiveData.value = HomeInfoViewState.Error(error)
                }
            ).addTo(compositeDisposable)
    }

    fun typesShiftRequest() {
        hRepository.typesShift()
            .subscribe(
                { list ->
                    shiftsLiveData.value =
                        HomeShiftViewState.SuccessTypesShift(list)
                },
                { error ->
                    shiftsLiveData.value = HomeShiftViewState.Error(error)
                }
            ).addTo(compositeDisposable)
    }

    fun selectTypeShiftRequest(type: String) {
        hRepository.selectTypeShift(type)
            .subscribe(
                { status ->
                    shiftsLiveData.value =
                        HomeShiftViewState.SuccessSelectShift(status.response)
                },
                { error ->
                    shiftsLiveData.value = HomeShiftViewState.Error(error)
                }
            ).addTo(compositeDisposable)
    }

    private fun startProgress() {
        infoLiveData.value = HomeInfoViewState.Loading()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}