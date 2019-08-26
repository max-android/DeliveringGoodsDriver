package com.my_project.deliveringgoods.viewmodels.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.my_project.deliveringgoods.App
import com.my_project.deliveringgoods.data.repository.SearchRepository
import com.my_project.deliveringgoods.router.Router
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class SearchViewModel: ViewModel() {

    @Inject
    lateinit var sRepository: SearchRepository
    @Inject
    lateinit var router: Router
    val sLiveData = MutableLiveData<SearchViewState>()
    private val compositeDisposable = CompositeDisposable()

    init {
        App.appComponent.inject(this)
    }

    fun back() = router.back()
}