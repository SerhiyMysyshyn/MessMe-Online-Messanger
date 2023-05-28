package com.serhiymysyshyn.messme.data.account

import com.serhiymysyshyn.messme.domain.account.AccountEntity
import com.serhiymysyshyn.messme.domain.type.Either
import com.serhiymysyshyn.messme.domain.type.None
import com.serhiymysyshyn.messme.domain.type.Failure

interface AccountCache {
    fun getToken(): Either<Failure, String>
    fun saveToken(token: String): Either<Failure, None>

    fun logout(): Either<Failure, None>

    fun getCurrentAccount(): Either<Failure, AccountEntity>
    fun saveAccount(account: AccountEntity): Either<Failure, None>

    fun checkAuth(): Either<Failure, Boolean>
}