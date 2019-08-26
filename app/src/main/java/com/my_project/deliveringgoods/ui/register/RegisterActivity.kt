package com.my_project.deliveringgoods.ui.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import com.my_project.deliveringgoods.App
import com.my_project.deliveringgoods.R
import com.my_project.deliveringgoods.data.provider.ResourceProvider
import com.my_project.deliveringgoods.ui.base.BaseActivity
import com.my_project.deliveringgoods.ui.main.MainActivity
import com.my_project.deliveringgoods.utilities.DialogUtils
import com.my_project.deliveringgoods.viewmodels.register.RegisterViewModel
import com.my_project.deliveringgoods.viewmodels.register.RegisterViewState
import gone
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_login.loginTextInputEditText
import kotlinx.android.synthetic.main.activity_login.passwordTextInputEditText
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.preloader.*
import timber.log.Timber
import visible
import java.net.ConnectException
import javax.inject.Inject

class RegisterActivity : BaseActivity() {

    companion object {
        fun newInstance(context: Context) = Intent(context, RegisterActivity::class.java)
    }

    @Inject
    lateinit var rProvider: ResourceProvider
    private val subscriptions = CompositeDisposable()
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
        setContentView(R.layout.activity_register)
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        init()
        observeData()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        subscriptions.dispose()
        super.onDestroy()
    }

    private fun init() {
        initActionBar()
        observeSignInButtonClicks()
        observeSignInButtonState()
    }

    private fun initActionBar() {
        val actionBar = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        title = rProvider.getString(R.string.registration)
    }

    private fun observeSignInButtonClicks() = registerButton.clicks().subscribe { signIn() }.addTo(subscriptions)

    private fun observeSignInButtonState() {
            Observables.combineLatest(
                loginTextInputEditText.textChanges(),
                mailTextInputEditText.textChanges(),
                passwordTextInputEditText.textChanges()
            ) { login,mail, password ->
                login.length >= 3 && login.isNotBlank() && mail.isNotBlank() && password.length >= 3 && password.isNotBlank()
            }.subscribe { signInButtonEnabled ->
                registerButton.isEnabled = signInButtonEnabled
            }.addTo(subscriptions)
    }

    private fun signIn() {
        val login = loginTextInputEditText.text.toString()
        val mail = mailTextInputEditText.text.toString()
        val password = passwordTextInputEditText.text.toString()
            viewModel.register(login,mail, password)
    }

    private fun observeData() = viewModel.registerLiveData.observe(this, Observer { processViewState(it) })

    private fun processViewState(viewState: RegisterViewState?) {
        viewState?.let{
            when (it) {
                is RegisterViewState.Loading -> showProgress()
                is RegisterViewState.SuccessRegistration -> launchMain()
                is RegisterViewState.Error -> showError(it.error)
            }
        }
    }

    private fun showProgress() = preloaderLinearLayout.visible()

    private fun removeProgress() = preloaderLinearLayout.gone()

    private fun launchMain() {
        removeProgress()
        startActivity(MainActivity.newInstance(this))
    }

    private fun showError(error: Throwable){
        Timber.e(error)
        removeProgress()
        if (error is ConnectException)
            DialogUtils(this).showNoConnect()
    }
}
