package com.my_project.deliveringgoods.ui.splash

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.my_project.deliveringgoods.ui.base.BaseActivity
import com.my_project.deliveringgoods.ui.login.LoginActivity
import com.my_project.deliveringgoods.ui.main.MainActivity
import com.my_project.deliveringgoods.viewmodels.splash.SplashViewModel
import com.my_project.deliveringgoods.viewmodels.splash.SplashViewState

class SplashActivity : BaseActivity() {

    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
        observeRegister()
        viewModel.user()
    }

    private fun observeRegister() = viewModel.sLiveData.observe(this, Observer { processViewState(it) })

    private fun processViewState(viewState: SplashViewState?) {
        viewState?.let {
            when (it) {
                is SplashViewState.SuccessExistUser -> {
                    if (it.status) launchMain() else launchLogin()
                }
            }
        }
    }

    private fun launchMain() {
        startActivity(MainActivity.newInstance(this))
        finish()
    }

    private fun launchLogin() {
        startActivity(LoginActivity.newInstance(this))
        finish()
    }
}
