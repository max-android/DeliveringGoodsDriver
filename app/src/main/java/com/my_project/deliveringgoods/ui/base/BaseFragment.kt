package com.my_project.deliveringgoods.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.my_project.deliveringgoods.utilities.DialogUtils
import gone
import kotlinx.android.synthetic.main.preloader.*
import timber.log.Timber
import visible
import java.net.ConnectException

abstract class BaseFragment: Fragment()  {

    @LayoutRes
    protected abstract fun getLayoutRes(): Int

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutRes(), container, false)
    }

    protected fun showProgress() = preloaderLinearLayout.visible()

    protected fun removeProgress() = preloaderLinearLayout.gone()

    open fun showError(error: Throwable) {
        Timber.e(error)
        removeProgress()
        if (error is ConnectException)
            DialogUtils(context!!).showNoConnect()
    }

}