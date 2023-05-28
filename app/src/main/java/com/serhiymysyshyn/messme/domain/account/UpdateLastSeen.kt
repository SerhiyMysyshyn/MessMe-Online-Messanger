package com.serhiymysyshyn.messme.domain.account

import com.serhiymysyshyn.messme.domain.interactor.UseCase
import com.serhiymysyshyn.messme.domain.type.None
import javax.inject.Inject

class UpdateLastSeen @Inject constructor(
    private val accountRepository: AccountRepository
) : UseCase<None, None>() {

    override suspend fun run(params: None) = accountRepository.updateAccountLastSeen()
}