package com.serhiymysyshyn.messme.domain.account

import com.serhiymysyshyn.messme.domain.type.None
import com.serhiymysyshyn.messme.domain.interactor.UseCase
import javax.inject.Inject

class UpdateToken @Inject constructor(
    private val accountRepository: AccountRepository
) : UseCase<None, UpdateToken.Params>() {

    override suspend fun run(params: Params) = accountRepository.updateAccountToken(params.token)

    data class Params(val token: String)
}