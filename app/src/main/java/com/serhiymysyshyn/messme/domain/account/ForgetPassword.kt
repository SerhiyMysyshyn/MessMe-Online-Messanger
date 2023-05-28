package com.serhiymysyshyn.messme.domain.account

import com.serhiymysyshyn.messme.domain.interactor.UseCase
import com.serhiymysyshyn.messme.domain.type.None
import javax.inject.Inject

class ForgetPassword @Inject constructor(
    private val accountRepository: AccountRepository
) : UseCase<None, ForgetPassword.Params>() {

    override suspend fun run(params: Params) = accountRepository.forgetPassword(params.email)

    data class Params(val email: String)
}