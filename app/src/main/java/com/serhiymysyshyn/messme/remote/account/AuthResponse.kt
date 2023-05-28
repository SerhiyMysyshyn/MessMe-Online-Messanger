package com.serhiymysyshyn.messme.remote.account

import com.serhiymysyshyn.messme.domain.account.AccountEntity
import com.serhiymysyshyn.messme.remote.core.BaseResponse

class AuthResponse(
    success: Int,
    message: String,
    val user: AccountEntity
) : BaseResponse(success, message)