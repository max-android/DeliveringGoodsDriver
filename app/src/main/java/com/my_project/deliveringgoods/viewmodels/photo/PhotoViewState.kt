package com.my_project.deliveringgoods.viewmodels.photo

import android.graphics.Bitmap


sealed class PhotoViewState {
    class SuccessSendFile(val result: Boolean): PhotoViewState()
    class SuccessSaveCarImage(val save: Boolean): PhotoViewState()
    class SuccessGetCarImage(val bitmap: Bitmap): PhotoViewState()
    class Loading: PhotoViewState()
    class Error(val error: Throwable): PhotoViewState()
}