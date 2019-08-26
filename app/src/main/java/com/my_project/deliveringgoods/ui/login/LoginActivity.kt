package com.my_project.deliveringgoods.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import com.my_project.deliveringgoods.R
import com.my_project.deliveringgoods.ui.base.BaseActivity
import com.my_project.deliveringgoods.ui.main.MainActivity
import com.my_project.deliveringgoods.ui.register.RegisterActivity
import com.my_project.deliveringgoods.utilities.DialogUtils
import com.my_project.deliveringgoods.viewmodels.login.LoginViewModel
import com.my_project.deliveringgoods.viewmodels.login.LoginViewState
import gone
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.preloader.*
import timber.log.Timber
import visible
import java.net.ConnectException

class LoginActivity : BaseActivity() {

    companion object {
        @JvmStatic
        fun newInstance(context: Context): Intent = Intent(context, LoginActivity::class.java)
    }

    private val subscriptions = CompositeDisposable()
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        init()
        observeData()
    }

    override fun onDestroy() {
        subscriptions.dispose()
        super.onDestroy()
    }

    private fun init() {
        observeSignInButtonClicks()
        observeSignInButtonState()
        observeRegister()
    }

    private fun observeSignInButtonClicks() = signInButton.clicks().subscribe { signIn() }.addTo(subscriptions)

    private fun observeSignInButtonState() {
        Observables.combineLatest(
            loginTextInputEditText.textChanges(),
            passwordTextInputEditText.textChanges()
        ) { login, password ->
            login.length >= 3 && login.isNotBlank() && password.length >= 3 && password.isNotBlank()
        }.subscribe { signInButtonEnabled ->
            signInButton.isEnabled = signInButtonEnabled
        }.addTo(subscriptions)
    }

    private fun observeRegister() = registerTextView.clicks().subscribe { launchRegister() }.addTo(subscriptions)

    private fun signIn() {
        val login = loginTextInputEditText.text.toString()
        val password = passwordTextInputEditText.text.toString()
        viewModel.login(login, password)
    }

    private fun observeData() = viewModel.loginLiveData.observe(this, Observer { processViewState(it) })

    private fun processViewState(viewState: LoginViewState?) {
        viewState?.let {
            when (it) {
                is LoginViewState.Loading -> showProgress()
                is LoginViewState.SuccessLogin -> launchMain()
                is LoginViewState.Error -> showError(it.error)
            }
        }
    }

    private fun showProgress() = preloaderLinearLayout.visible()

    private fun removeProgress() = preloaderLinearLayout.gone()

    private fun launchMain() {
        removeProgress()
        startActivity(MainActivity.newInstance(this))
    }

    private fun launchRegister() = startActivity(
        RegisterActivity.newInstance(
            this
        )
    )

    private fun showError(error: Throwable) {
        Timber.e(error)
        removeProgress()
        if (error is ConnectException)
            DialogUtils(this).showNoConnect()
    }
}



