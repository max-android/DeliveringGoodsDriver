package com.my_project.deliveringgoods.ui.profile

import CONST
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.my_project.deliveringgoods.App
import com.my_project.deliveringgoods.R
import com.my_project.deliveringgoods.data.file_storage.InternalStorage
import com.my_project.deliveringgoods.data.provider.ResourceProvider
import com.my_project.deliveringgoods.ui.base.BaseFragment
import com.my_project.deliveringgoods.ui.login.LoginActivity
import com.my_project.deliveringgoods.utilities.WritePermission
import com.my_project.deliveringgoods.viewmodels.profile.ProfileViewModel
import com.my_project.deliveringgoods.viewmodels.profile.ProfileViewState
import gone
import kotlinx.android.synthetic.main.fragment_profile.*
import mainActivity
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.support.v4.toast
import javax.inject.Inject

class ProfileFragment : BaseFragment() {

    companion object {
        const val PROFILE_IMAGE_REQUEST_CODE = 103
        @JvmStatic
        fun newInstance(): Fragment = ProfileFragment()
    }

    @Inject
    lateinit var rProvider: ResourceProvider
    private lateinit var viewModel: ProfileViewModel

    override fun getLayoutRes(): Int = R.layout.fragment_profile

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        App.appComponent.inject(this)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        init()
        observeData()
        if (savedInstanceState == null && viewModel.checkExistProfileImage()) {
            viewModel.readProfileImageRequest(InternalStorage.IMAGE_PROFILE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == WritePermission.WRITE_FILE_PERMISSION_REQUEST_CODE)
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                view!!.snackbar(com.my_project.deliveringgoods.R.string.permission_write_granted)
                Handler().postDelayed(::pickImage, CONST.GALLERY_DELAY)
            } else {
                view!!.snackbar(com.my_project.deliveringgoods.R.string.permission_write_denied)
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PROFILE_IMAGE_REQUEST_CODE && data != null) {
            data.data?.let {
                saveProfileIntoStorage(it.toString())
                showProfile(it)
            }
        }
    }

    private fun init() {
        updateToolbar()
        setListeners()
    }

    private fun updateToolbar() {
        mainActivity?.toolbar?.gone()
    }

    private fun setListeners() {
        logoutButton.setOnClickListener { viewModel.logoutRequest() }
        avatarCircleImage.setOnClickListener { WritePermission().requestPermission(context!!, ::pickImage) }
        photoButton.setOnClickListener { viewModel.photo() }
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PROFILE_IMAGE_REQUEST_CODE)
    }

    private fun observeData() = viewModel.pLiveData.observe(this, Observer { processViewState(it) })

    private fun processViewState(viewState: ProfileViewState?) {
        viewState?.let {
            when (it) {
                is ProfileViewState.Loading -> showProgress()
                is ProfileViewState.SuccessLogout -> updateProfile(it.result)
                is ProfileViewState.SuccessSaveProfileImage -> saveProfileImageMessage(it.save)
                is ProfileViewState.SuccessProfileImage -> setProfileImage(it.url)
                is ProfileViewState.Error -> showError(it.error)
            }
        }
    }

    private fun updateProfile(status: Boolean) {
        removeProgress()
        if (status) launchLogin() else toast(rProvider.getString(R.string.logout_error))
    }

    private fun saveProfileImageMessage(save: Boolean) {
        if (save) toast(rProvider.getString(R.string.profile_save))
        else toast(rProvider.getString(R.string.profile_save_error))
    }

    private fun setProfileImage(url: String) {
        if (url == InternalStorage.EMPTY_PROFILE)
            return
        val uri = Uri.parse(url)
        avatarCircleImage.setImageURI(uri)
        animateProfileImage()
    }

    private fun launchLogin() {
        startActivity(LoginActivity.newInstance(context!!))
        mainActivity?.finish()
    }

    private fun saveProfileIntoStorage(url: String) =
        viewModel.writeProfileImageRequest(InternalStorage.IMAGE_PROFILE, url)

    private fun showProfile(uri: Uri) {
        avatarCircleImage.setImageURI(uri)
    }

    private fun animateProfileImage() {
        val animation = AnimationUtils.loadAnimation(context!!, R.anim.show_view)
        avatarCircleImage.startAnimation(animation)
    }
}