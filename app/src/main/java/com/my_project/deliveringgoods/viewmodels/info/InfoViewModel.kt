package com.my_project.deliveringgoods.viewmodels.info

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.my_project.deliveringgoods.App
import com.my_project.deliveringgoods.data.repository.InfoRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class InfoViewModel : ViewModel() {

    @Inject
    lateinit var iRepository: InfoRepository
    val iLiveData = MutableLiveData<InfoViewState>()
    private val compositeDisposable = CompositeDisposable()

    init {
        App.appComponent.inject(this)
    }

    fun userAgreementRequest() {
        iRepository.agreement()
            .doOnSubscribe { startProgress() }
            .subscribe(
                { agreement -> iLiveData.value = InfoViewState.SuccessUserAgreement(agreement) },
                { error -> iLiveData.value = InfoViewState.Error(error) }
            ).addTo(compositeDisposable)
    }

    fun privacyPolicyRequest() {
        iRepository.privacy()
            .doOnSubscribe { startProgress() }
            .subscribe(
                { privacy -> iLiveData.value = InfoViewState.SuccessPrivacyPolicy(privacy) },
                { error -> iLiveData.value = InfoViewState.Error(error) }
            ).addTo(compositeDisposable)
    }

    fun termsDeliveryRequest() {
        iRepository.terms()
            .doOnSubscribe { startProgress() }
            .subscribe(
                { terms -> iLiveData.value = InfoViewState.SuccessTermsDelivery(terms) },
                { error -> iLiveData.value = InfoViewState.Error(error) }
            ).addTo(compositeDisposable)
    }

    private fun startProgress() {
        iLiveData.value = InfoViewState.Loading()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}