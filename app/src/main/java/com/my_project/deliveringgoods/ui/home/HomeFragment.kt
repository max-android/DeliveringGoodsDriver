package com.my_project.deliveringgoods.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.my_project.deliveringgoods.App
import com.my_project.deliveringgoods.R
import com.my_project.deliveringgoods.data.entities.AdvInfo
import com.my_project.deliveringgoods.data.entities.TypesShift
import com.my_project.deliveringgoods.data.provider.ResourceProvider
import com.my_project.deliveringgoods.ui.adapters.HomeAdapter
import com.my_project.deliveringgoods.ui.base.BaseFragment
import com.my_project.deliveringgoods.utilities.DialogUtils
import com.my_project.deliveringgoods.viewmodels.home.HomeInfoViewState
import com.my_project.deliveringgoods.viewmodels.home.HomeShiftViewState
import com.my_project.deliveringgoods.viewmodels.home.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import mainActivity
import javax.inject.Inject


class HomeFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(): Fragment = HomeFragment()
    }

    @Inject
    lateinit var rProvider: ResourceProvider
    private lateinit var viewModel: HomeViewModel
    var homeAdapter: HomeAdapter? = null

    override fun getLayoutRes(): Int = R.layout.fragment_home

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        App.appComponent.inject(this)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        init()
        observeData()
        if (savedInstanceState == null) {
            infoRequest()
            typesShiftRequest()
        }
    }

    private fun init() {
        updateToolbar()
        initAdapter()
        setSpinnerListener()
    }

    private fun updateToolbar() {
        mainActivity?.updateToolbar(rProvider.getString(R.string.app_name_top), false)
        mainActivity?.changeMenuToolBar(true)
    }

    private fun initAdapter() {
        homeAdapter = HomeAdapter()
        homeViewPager.adapter = homeAdapter
        homeTabLayout.setupWithViewPager(homeViewPager, true)
    }

    private fun setSpinnerListener() {
        shiftSpinner.setOnItemSelectedListener { _, _, _, item ->
            (item as? TypesShift)?.type?.let { selectTypeShiftRequest(it) }
        }
    }

    private fun infoRequest() = viewModel.infoRequest()
    private fun typesShiftRequest() = viewModel.typesShiftRequest()
    private fun selectTypeShiftRequest(type: String) = viewModel.selectTypeShiftRequest(type)

    private fun observeData() {
        viewModel.infoLiveData.observe(this, Observer { infoProcessViewState(it) })
        viewModel.shiftsLiveData.observe(this, Observer { shiftProcessViewState(it) })
    }

    private fun infoProcessViewState(infoViewState: HomeInfoViewState?) {
        infoViewState?.let {
            when (it) {
                is HomeInfoViewState.Loading -> showProgress()
                is HomeInfoViewState.SuccessInfo -> updateAdapter(it.info)
                is HomeInfoViewState.Error -> showError(it.error)
            }
        }
    }

    private fun shiftProcessViewState(shiftViewState: HomeShiftViewState?) {
        shiftViewState?.let {
            when (it) {
                is HomeShiftViewState.SuccessTypesShift -> showTypesShift(it.types)
                is HomeShiftViewState.SuccessSelectShift -> showResultSelectShift(it.status)
                is HomeShiftViewState.Error -> showError(it.error)
            }
        }
    }

    private fun updateAdapter(data: List<AdvInfo>) {
        homeAdapter?.addData(data)
        homeAdapter?.notifyDataSetChanged()
        removeProgress()
    }

    private fun showTypesShift(types: List<TypesShift>) {
        shiftSpinner.setItems(types.map { it.type })
    }

    private fun showResultSelectShift(status: Boolean) {
        if (status) DialogUtils(context!!).showMessage(
            rProvider.getString(R.string.select_shift_title)
            , rProvider.getString(R.string.select_shift_success)
        )
        else DialogUtils(context!!).showMessage(
            rProvider.getString(R.string.select_shift_title)
            , rProvider.getString(R.string.select_shift_no_success)
        )
    }

}