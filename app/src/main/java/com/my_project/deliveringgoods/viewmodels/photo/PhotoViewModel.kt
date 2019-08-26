package com.my_project.deliveringgoods.viewmodels.photo

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.my_project.deliveringgoods.App
import com.my_project.deliveringgoods.data.repository.PhotoRepository
import com.my_project.deliveringgoods.router.Router
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import timber.log.Timber
import javax.inject.Inject


class PhotoViewModel : ViewModel() {

    @Inject
    lateinit var pRepository: PhotoRepository
    @Inject
    lateinit var router: Router
    val pLiveData = MutableLiveData<PhotoViewState>()
    private val compositeDisposable = CompositeDisposable()

    init {
        App.appComponent.inject(this)
    }

   fun sendImageRequest() {
        pRepository.sendFile()
            .doOnSubscribe { startProgress() }
            .subscribe(
                { result -> pLiveData.value = PhotoViewState.SuccessSendFile(result.response) },
                { error -> pLiveData.value = PhotoViewState.Error(error) }
            ).addTo(compositeDisposable)
    }

    fun writeImageRequest(image: Bitmap) {
        pRepository.writeImageBitmap(image)
            .subscribe(
                { pLiveData.value = PhotoViewState.SuccessSaveCarImage(true) },
                { error ->
                    run {
                        Timber.e(error)
                        pLiveData.value = PhotoViewState.SuccessSaveCarImage(false)
                    }
                }
            ).addTo(compositeDisposable)
    }

    fun readImageRequest(@DrawableRes defaultId: Int) {
        pRepository.readImageBitmap(defaultId)
            .doOnSubscribe { startProgress() }
            .subscribe(
                { image -> pLiveData.value = PhotoViewState.SuccessGetCarImage(image) },
                { error -> pLiveData.value = PhotoViewState.Error(error) }
            ).addTo(compositeDisposable)
    }

    fun back() = router.back()

    private fun startProgress() {
        pLiveData.value = PhotoViewState.Loading()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}