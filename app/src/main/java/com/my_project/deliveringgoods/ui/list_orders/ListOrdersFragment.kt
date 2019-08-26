package com.my_project.deliveringgoods.ui.list_orders

import CONST
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.my_project.deliveringgoods.App
import com.my_project.deliveringgoods.R
import com.my_project.deliveringgoods.data.entities.Order
import com.my_project.deliveringgoods.data.provider.ResourceProvider
import com.my_project.deliveringgoods.ui.adapters.ListTasksAdapter
import com.my_project.deliveringgoods.ui.base.BaseFragment
import com.my_project.deliveringgoods.viewmodels.list_orders.ListOrdersViewModel
import com.my_project.deliveringgoods.viewmodels.list_orders.ListOrdersViewState
import gone
import kotlinx.android.synthetic.main.fragment_list_orders.*
import mainActivity
import visible
import javax.inject.Inject

class ListOrdersFragment : BaseFragment() {

    companion object {
        const val LIST_TASK_KEY = "list_task_key"
        @JvmStatic
        fun newInstance(): Fragment = ListOrdersFragment()
    }

    @Inject
    lateinit var rProvider: ResourceProvider
    private lateinit var viewModel: ListOrdersViewModel
    private lateinit var listTasksAdapter: ListTasksAdapter
    private var updateScreen = true

    override fun getLayoutRes(): Int = R.layout.fragment_list_orders

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        App.appComponent.inject(this)
        viewModel = ViewModelProvider(this).get(ListOrdersViewModel::class.java)
        init()
        observeData()
        if (savedInstanceState == null && updateScreen) {
            viewModel.ordersRequest()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        updateScreen = false
    }

    private fun init() {
        updateToolbar()
        initSwipe()
        initAdapter()
    }

    private fun updateToolbar() {
        mainActivity?.updateToolbar(rProvider.getString(R.string.tasks), false)
        mainActivity?.changeMenuToolBar(false)
    }

    private fun updateBottomNavigation(size: String) =
        mainActivity?.bottomNavigation?.setNotification(size, CONST.POSITION_ORDERS)

    private fun initAdapter() {
        listTasksAdapter = ListTasksAdapter { item, view -> onItemOrderClick(item, view) }
        ordersRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ListOrdersFragment.context, RecyclerView.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(this@ListOrdersFragment.context, LinearLayout.VERTICAL))
            adapter = listTasksAdapter
        }
    }

    private fun initSwipe() {
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN)
        swipeRefreshLayout.setOnRefreshListener { refreshScreen() }
    }

    private fun refreshScreen() {
        swipeRefreshLayout.isRefreshing = true
        viewModel.ordersRequest()
        swipeRefreshLayout.isRefreshing = false
    }

    private fun observeData() = viewModel.loLiveData.observe(this, Observer { processViewState(it) })

    private fun processViewState(viewState: ListOrdersViewState?) {
        viewState?.let {
            when (it) {
                is ListOrdersViewState.Loading -> showLoading()
                is ListOrdersViewState.SuccessOrders -> showOrders(it.orders)
                is ListOrdersViewState.Error -> showError(it.error)
            }
        }
    }

    private fun showLoading() {
        showProgress()
        emptyOrdersImageView.gone()
        ordersRecyclerView.gone()
    }

    private fun showOrders(orders: List<Order>) {
        listTasksAdapter.submitList(orders)
        updateBottomNavigation(orders.size.toString())
        removeProgress()
        emptyOrdersImageView.gone()
        ordersRecyclerView.visible()
    }

    override fun showError(error: Throwable) {
        super.showError(error)
        emptyOrdersImageView.visible()
        ordersRecyclerView.gone()
    }

    private fun onItemOrderClick(order: Order, view: View) {
        viewModel.onClickItem(order)
    }

}