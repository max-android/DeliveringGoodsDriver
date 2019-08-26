package com.my_project.deliveringgoods.ui.order

import CONST
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.my_project.deliveringgoods.App
import com.my_project.deliveringgoods.R
import com.my_project.deliveringgoods.data.entities.Order
import com.my_project.deliveringgoods.data.provider.ResourceProvider
import com.my_project.deliveringgoods.ui.base.BaseFragment
import com.my_project.deliveringgoods.utilities.DialogUtils
import com.my_project.deliveringgoods.utilities.getCurrentTime
import com.my_project.deliveringgoods.utilities.getMillis
import com.my_project.deliveringgoods.viewmodels.order.OrderViewModel
import com.my_project.deliveringgoods.viewmodels.order.OrderViewState
import kotlinx.android.synthetic.main.fragment_order.*
import mainActivity
import javax.inject.Inject


class OrderFragment:BaseFragment() {

    companion object {
        private const val TASK_KEY = "task_key"

        @JvmStatic
        fun newInstance(order: Order): Fragment = OrderFragment().apply {
            arguments = Bundle().apply {
                putParcelable(TASK_KEY, order)
            }
        }
    }

    @Inject
    lateinit var rProvider: ResourceProvider
    private lateinit var viewModel: OrderViewModel
    private var order:Order? = null

    override fun getLayoutRes(): Int = R.layout.fragment_order

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        App.appComponent.inject(this)
        viewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
        initData()
        init()
        observeData()
    }

    private fun initData(){
        order = arguments?.getParcelable(TASK_KEY) as? Order
    }

    private fun init(){
        initToolbar()
        setListener()
        updateUi()
    }

    private fun initToolbar(){
        mainActivity?.updateToolbar(rProvider.getStringForm(R.string.number_order_value,order?.id.toString()),true)
        mainActivity?.changeMenuToolBar(false)
    }

    private fun setListener(){
        acceptOrderButton.setOnClickListener {
            order?.let{
                DialogUtils(context!!).showMessage(
                    rProvider.getString(R.string.confirm_operation),
                    rProvider.getString(R.string.dialog_accept_order)
                    ){
                    viewModel.acceptOrderRequest(it.id.toString())
                }
            }
        }
    }

    private fun updateUi(){
        order?.let {
            descriptionTextView.text = it.description
            addressTextView.text = it.address
            val currentTime = context!!.getCurrentTime
            val time = (it.startTime.getMillis - currentTime)/CONST.TO_MINUTE
            if(time < 0) timeTextView.setTextColor(rProvider.getColor(R.color.red))
            else timeTextView.setTextColor(rProvider.getColor(R.color.green))
            timeTextView.text = rProvider.getStringForm(R.string.time_order,time.toString())
            priceTextView.text = rProvider.getStringForm(R.string.price_order,it.price.toString())
        }
    }

    private fun observeData() = viewModel.oLiveData.observe(this, Observer { processViewState(it) })

    private fun processViewState(viewState: OrderViewState?) {
        viewState?.let {
            when (it) {
                is OrderViewState.Loading -> showProgress()
                is OrderViewState.SuccessAcceptOrder -> showResultAcceptOrder(it.result)
                is OrderViewState.Error -> showError(it.error)
            }
        }
    }

    private fun showResultAcceptOrder(status:Boolean){
      if(status) DialogUtils(context!!).showBottomSheet(
          rProvider.getString(R.string.info_about_confirm_order),
          rProvider.getString(R.string.positive_confirm_order)
      ){ viewModel.back() }
      else
          DialogUtils(context!!).showBottomSheet(
              rProvider.getString(R.string.info_about_confirm_order),
              rProvider.getString(R.string.negative_confirm_order)
          )
    }

}