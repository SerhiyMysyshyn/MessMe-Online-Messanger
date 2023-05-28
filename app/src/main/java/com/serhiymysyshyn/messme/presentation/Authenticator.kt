package com.serhiymysyshyn.messme.presentation

import com.serhiymysyshyn.messme.domain.account.CheckAuth
import com.serhiymysyshyn.messme.domain.type.None
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Authenticator
@Inject constructor(
    val checkAuth: CheckAuth
) {
    fun userLoggedIn(body: (Boolean) -> Unit) {
        checkAuth(None()) {
            it.either({ body(false) }, { body(it) })
        }
    }
}