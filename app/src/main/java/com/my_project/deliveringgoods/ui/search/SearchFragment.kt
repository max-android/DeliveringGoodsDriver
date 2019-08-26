package com.my_project.deliveringgoods.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.my_project.deliveringgoods.R
import com.my_project.deliveringgoods.ui.base.BaseFragment
import com.my_project.deliveringgoods.viewmodels.search.SearchViewModel
import gone
import kotlinx.android.synthetic.main.fragment_search.*
import mainActivity

class SearchFragment : BaseFragment() {

    companion object {
        private const val SEARCH_KEY = "search_key"
        @JvmStatic
        fun newInstance(search: String): Fragment = SearchFragment().apply {
            arguments = Bundle().apply {
                putSerializable(SEARCH_KEY, search)
            }
        }
    }

    private lateinit var viewModel: SearchViewModel

    override fun getLayoutRes(): Int = R.layout.fragment_search

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        init()
    }

    private fun init() {
        updateToolbar()
        setBackButtonListener()
    }

    private fun updateToolbar() {
        mainActivity?.toolbar?.gone()
    }

    //кнопка назад будет своя - сейчас для теста
    private fun setBackButtonListener() {
        backButton.setOnClickListener { viewModel.back() }
    }

}