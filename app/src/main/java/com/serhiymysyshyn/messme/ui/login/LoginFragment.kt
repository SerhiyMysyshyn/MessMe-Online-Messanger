package com.serhiymysyshyn.messme.ui.login

import android.os.Bundle
import android.view.View
import com.serhiymysyshyn.messme.R
import com.serhiymysyshyn.messme.domain.account.AccountEntity
import com.serhiymysyshyn.messme.presentation.viewmodel.AccountViewModel
import com.serhiymysyshyn.messme.ui.App
import com.serhiymysyshyn.messme.ui.core.BaseFragment
import com.serhiymysyshyn.messme.ui.core.ext.onFailure
import com.serhiymysyshyn.messme.ui.core.ext.onSuccess
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_register.etEmail
import kotlinx.android.synthetic.main.fragment_register.etPassword

class LoginFragment : BaseFragment() {

    override val layoutId: Int = R.layout.fragment_login
    override val titleToolbar = R.string.screen_login

    private lateinit var accountViewModel: AccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)


        accountViewModel = viewModel {
            onSuccess(accountData, ::renderAccount)
            onFailure(failureData, ::handleFailure)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnLogin.setOnClickListener {
            validateFields()
        }

        btnRegister.setOnClickListener {
            activity?.let { navigator.showSignUp(it) }
        }

        btnForgetPassword.setOnClickListener {
            activity?.let { navigator.showForgedPassword(it) }
        }
    }

    private fun validateFields() {
        hideSoftKeyboard()
        val allFields = arrayOf(etEmail, etPassword)
        var allValid = true
        for (field in allFields) {
            allValid = field.testValidity() && allValid
        }
        if (allValid) {
            login(etEmail.text.toString(), etPassword.text.toString())
        }
    }

    private fun login(email: String, password: String) {
        showProgress()
        accountViewModel.login(email, password)
    }

    private fun renderAccount(account: AccountEntity?) {
        hideProgress()
        activity?.let {
            navigator.showHome(it)
            it.finish()
        }
    }
}