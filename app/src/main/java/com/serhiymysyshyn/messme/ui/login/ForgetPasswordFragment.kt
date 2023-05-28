package com.serhiymysyshyn.messme.ui.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.serhiymysyshyn.messme.R
import com.serhiymysyshyn.messme.domain.type.None
import com.serhiymysyshyn.messme.presentation.viewmodel.AccountViewModel
import com.serhiymysyshyn.messme.ui.App
import com.serhiymysyshyn.messme.ui.core.BaseFragment
import com.serhiymysyshyn.messme.ui.core.ext.onFailure
import com.serhiymysyshyn.messme.ui.core.ext.onSuccess
import kotlinx.android.synthetic.main.fragment_forget_password.*

class ForgetPasswordFragment : BaseFragment() {
    override val layoutId: Int = R.layout.fragment_forget_password
    override val titleToolbar: Int = R.string.screen_forget_password

    lateinit var accountViewModel: AccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accountViewModel = viewModel {
            onSuccess(forgetPasswordData, ::onPasswordSent)
            onFailure(failureData, ::handleFailure)
        }

        btnSendPassword.setOnClickListener {
            sendPassword()
        }
    }

    private fun sendPassword() {
        if (etEmail.testValidity()) {
            val email = etEmail.text.toString()
            accountViewModel.forgetPassword(email)
        }
    }

    private fun onPasswordSent(none: None?) {
        Toast.makeText(requireContext(), getString(R.string.email_sent), Toast.LENGTH_SHORT).show()
        activity?.finish()
    }
}