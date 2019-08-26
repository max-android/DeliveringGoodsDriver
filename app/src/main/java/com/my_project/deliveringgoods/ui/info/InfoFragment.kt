package com.my_project.deliveringgoods.ui.info

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.my_project.deliveringgoods.App
import com.my_project.deliveringgoods.R
import com.my_project.deliveringgoods.data.provider.ResourceProvider
import com.my_project.deliveringgoods.ui.base.BaseFragment
import com.my_project.deliveringgoods.viewmodels.info.InfoViewModel
import com.my_project.deliveringgoods.viewmodels.info.InfoViewState
import kotlinx.android.synthetic.main.fragment_info.*
import kotlinx.android.synthetic.main.layout_privacy_policy.*
import kotlinx.android.synthetic.main.layout_terms_delivery.*
import kotlinx.android.synthetic.main.layout_user_agreement.*
import mainActivity
import javax.inject.Inject


class InfoFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(): Fragment = InfoFragment()
    }

    @Inject
    lateinit var rProvider: ResourceProvider
    private lateinit var viewModel: InfoViewModel
    var selectView: View? = null

    override fun getLayoutRes(): Int = R.layout.fragment_info

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        App.appComponent.inject(this)
        viewModel = ViewModelProvider(this).get(InfoViewModel::class.java)
        init()
        observeData()
    }

    private fun init() {
        updateToolbar()
        setButtonListener()
    }

    private fun updateToolbar() {
        mainActivity?.updateToolbar(rProvider.getString(R.string.info), true)
        mainActivity?.changeMenuToolBar(false)
    }

    private fun setButtonListener() {
        userAgreementLinearLayout.setOnClickListener {
            userAgreementActiveState()
            privacyPolicyPassiveState()
            termsDeliveryPassiveState()
            viewModel.userAgreementRequest()
        }
        privacyPolicyLinearLayout.setOnClickListener {
            privacyPolicyActiveState()
            userAgreementPassiveState()
            termsDeliveryPassiveState()
            viewModel.privacyPolicyRequest()
        }
        termsDeliveryLinearLayout.setOnClickListener {
            termsDeliveryActiveState()
            userAgreementPassiveState()
            privacyPolicyPassiveState()
            viewModel.termsDeliveryRequest()
        }
    }

    private fun userAgreementActiveState() {
        userAgreementLinearLayout.background = rProvider.getDrawable(R.drawable.info_item_ripple)
        userAgreementTextView.setTextColor(rProvider.getColor(R.color.colorPrimary))
    }

    private fun userAgreementPassiveState() {
        userAgreementLinearLayout.setBackgroundColor(rProvider.getColor(R.color.white))
        userAgreementTextView.setTextColor(rProvider.getColor(R.color.black))
    }

    private fun privacyPolicyActiveState() {
        privacyPolicyLinearLayout.background = rProvider.getDrawable(R.drawable.info_item_ripple)
        privacyPolicyTextView.setTextColor(rProvider.getColor(R.color.colorPrimary))
    }

    private fun privacyPolicyPassiveState() {
        privacyPolicyLinearLayout.setBackgroundColor(rProvider.getColor(R.color.white))
        privacyPolicyTextView.setTextColor(rProvider.getColor(R.color.black))
    }

    private fun termsDeliveryActiveState() {
        termsDeliveryLinearLayout.background = rProvider.getDrawable(R.drawable.info_item_ripple)
        termsDeliveryTextView.setTextColor(rProvider.getColor(R.color.colorPrimary))
    }

    private fun termsDeliveryPassiveState() {
        termsDeliveryLinearLayout.setBackgroundColor(rProvider.getColor(R.color.white))
        termsDeliveryTextView.setTextColor(rProvider.getColor(R.color.black))
    }

    private fun observeData() = viewModel.iLiveData.observe(this, Observer { processViewState(it) })

    private fun processViewState(viewState: InfoViewState?) {
        viewState?.let {
            when (it) {
                is InfoViewState.Loading -> showProgress()
                is InfoViewState.SuccessUserAgreement -> showUserAgreementText(it.userAgreement.agreement)
                is InfoViewState.SuccessPrivacyPolicy -> showPrivacyPolicyText(it.privacyPolicy.privacy)
                is InfoViewState.SuccessTermsDelivery -> showTermsDeliveryText(it.termsDelivery.terms)
                is InfoViewState.Error -> showError(it.error)
            }
        }
    }

    private fun showUserAgreementText(text: String) {
        updateRootView(R.layout.layout_user_agreement)
        agreementTextView.text = text
        removeProgress()
    }

    private fun showPrivacyPolicyText(text: String) {
        updateRootView(R.layout.layout_privacy_policy)
        privacyTextView.text = text
        removeProgress()
    }

    private fun showTermsDeliveryText(text: String) {
        updateRootView(R.layout.layout_terms_delivery)
        termsTextView.text = text
        removeProgress()
    }

    private fun updateRootView(layout: Int) {
        selectView?.let { viewsFrameLayout.removeView(it) }
        selectView = layoutInflater.inflate(layout, viewsFrameLayout, false)
        viewsFrameLayout.addView(selectView)
    }
}