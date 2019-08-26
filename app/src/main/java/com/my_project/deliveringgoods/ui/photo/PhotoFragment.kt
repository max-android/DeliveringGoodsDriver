package com.my_project.deliveringgoods.ui.photo

import CONST
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.my_project.deliveringgoods.App
import com.my_project.deliveringgoods.R
import com.my_project.deliveringgoods.data.provider.ResourceProvider
import com.my_project.deliveringgoods.ui.base.BaseFragment
import com.my_project.deliveringgoods.utilities.CameraPermission
import com.my_project.deliveringgoods.viewmodels.photo.PhotoViewModel
import com.my_project.deliveringgoods.viewmodels.photo.PhotoViewState
import gone
import kotlinx.android.synthetic.main.fragment_photo.*
import mainActivity
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.support.v4.longToast
import javax.inject.Inject

class PhotoFragment : BaseFragment() {

    companion object {
        const val CAMERA_REQUEST_CODE = 7777
        @JvmStatic
        fun newInstance(): Fragment = PhotoFragment()
    }

    private lateinit var viewModel: PhotoViewModel
    @Inject
    lateinit var rProvider: ResourceProvider

    override fun getLayoutRes(): Int = R.layout.fragment_photo

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        App.appComponent.inject(this)
        viewModel = ViewModelProvider(this).get(PhotoViewModel::class.java)
        init()
        readImageRequest()
        observeData()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == CameraPermission.CAMERA_PERMISSION_REQUEST_CODE)
            if (grantResults.size == 2
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                view!!.snackbar(com.my_project.deliveringgoods.R.string.permission_camera_granted)
                Handler().postDelayed(::launchCamera, CONST.GALLERY_DELAY)
            } else {
                view!!.snackbar(com.my_project.deliveringgoods.R.string.permission_camera_denied)
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    showImage(data)
                }
            }
        }
    }

    private fun askPermissions() {
        CameraPermission().requestPermission(context!!) {
            launchCamera()
        }
    }

    private fun readImageRequest() = viewModel.readImageRequest(R.drawable.ic_photo)

    private fun init() {
        updateToolbar()
        setListener()
    }

    private fun updateToolbar() {
        mainActivity?.toolbar?.gone()
    }

    private fun setListener() {
        doPhotoButton.setOnClickListener { askPermissions() }
    }

    private fun launchCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(context!!.packageManager) != null) {
            startActivityForResult(intent, CAMERA_REQUEST_CODE)

        }
    }

    private fun observeData() = viewModel.pLiveData.observe(this, Observer { processViewState(it) })

    private fun processViewState(viewState: PhotoViewState?) {
        viewState?.let {
            when (it) {
                is PhotoViewState.Loading -> showProgress()
                is PhotoViewState.SuccessGetCarImage -> setImage(it.bitmap)
                is PhotoViewState.SuccessSaveCarImage -> showSave(it.save)
                is PhotoViewState.SuccessSendFile -> showResult(it.result)
                is PhotoViewState.Error -> showError(it.error)
            }
        }
    }

    private fun setImage(bitmapImage: Bitmap) {
        carImageView.setImageBitmap(bitmapImage)
        removeProgress()
    }

    private fun showSave(save: Boolean) {
        if (save) sendImageRequest() else longToast(rProvider.getString(R.string.error_save_photo))
    }

    private fun showResult(result: Boolean) {
        if (!result) longToast(rProvider.getString(R.string.error_send_photo))
    }

    private fun sendImageRequest() = viewModel.sendImageRequest()

    private fun showImage(data: Intent?) {
        data?.let {
            if (it.hasExtra("data")) {
                val bitmapImage = data.getParcelableExtra("data") as? Bitmap
                if (bitmapImage != null) {
                    carImageView.setImageBitmap(bitmapImage)
                    saveToExternalStorage(bitmapImage)
                }
            }
        }
    }

    private fun saveToExternalStorage(bitmapImage: Bitmap) = viewModel.writeImageRequest(bitmapImage)

}

