package com.serhiymysyshyn.messme.domain.account

import com.serhiymysyshyn.messme.domain.interactor.UseCase
import com.serhiymysyshyn.messme.domain.type.Either
import com.serhiymysyshyn.messme.domain.type.Failure
import javax.inject.Inject

class EditAccount @Inject constructor(
    private val repository: AccountRepository
) : UseCase<AccountEntity, AccountEntity>() {

    override suspend fun run(params: AccountEntity): Either<Failure, AccountEntity> {
        return repository.editAccount(params)
    }
}