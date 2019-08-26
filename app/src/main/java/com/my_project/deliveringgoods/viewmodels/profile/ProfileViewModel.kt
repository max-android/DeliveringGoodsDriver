package com.my_project.deliveringgoods.viewmodels.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.my_project.deliveringgoods.App
import com.my_project.deliveringgoods.data.repository.ProfileRepository
import com.my_project.deliveringgoods.router.Router
import com.my_project.deliveringgoods.router.Screen
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import timber.log.Timber
import javax.inject.Inject

class ProfileViewModel : ViewModel() {

    @Inject
    lateinit var pRepository: ProfileRepository
    @Inject
    lateinit var router: Router
    val pLiveData = MutableLiveData<ProfileViewState>()
    private val compositeDisposable = CompositeDisposable()

    init {
        App.appComponent.inject(this)
    }

    fun logoutRequest() {
        pRepository.logout()
            .doOnSuccess { pRepository.user().deleteDataUser() }
            .doOnSubscribe { startProgress() }
            .subscribe(
                { result ->
                    pLiveData.value =
                        ProfileViewState.SuccessLogout(result.response)
                },
                { error ->
                    pLiveData.value = ProfileViewState.Error(error)
                }
            ).addTo(compositeDisposable)
    }


    fun writeProfileImageRequest(filename: String, fileContents: String) {
        pRepository.writeProfileImage(filename, fileContents)
            .subscribe(
                { pLiveData.value = ProfileViewState.SuccessSaveProfileImage(true) },
                { error ->
                    run {
                        Timber.e(error)
                        pLiveData.value = ProfileViewState.SuccessSaveProfileImage(false)
                    }
                }
            ).addTo(compositeDisposable)
    }

    fun readProfileImageRequest(filename: String) {
        pRepository.readProfileImage(filename)
            .subscribe(
                { url -> pLiveData.value = ProfileViewState.SuccessProfileImage(url) },
                { error -> pLiveData.value = ProfileViewState.Error(error) }
            ).addTo(compositeDisposable)
    }

    fun checkExistProfileImage() = pRepository.checkExistProfileImage()

    fun photo() = router.forward(Screen.PHOTO)

    private fun startProgress() {
        pLiveData.value = ProfileViewState.Loading()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}