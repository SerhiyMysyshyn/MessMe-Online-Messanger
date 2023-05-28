package com.serhiymysyshyn.messme.domain.account

import com.serhiymysyshyn.messme.domain.interactor.UseCase
import javax.inject.Inject

class Login @Inject constructor(
    private val accountRepository: AccountRepository
) : UseCase<AccountEntity, Login.Params>() {

    override suspend fun run(params: Params) = accountRepository.login(params.email, params.password)

    data class Params(val email: String, val password: String)
}