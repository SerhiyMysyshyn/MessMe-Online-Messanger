package com.serhiymysyshyn.messme.data.notifications

import com.serhiymysyshyn.messme.domain.type.Either
import com.serhiymysyshyn.messme.domain.type.Failure
import com.serhiymysyshyn.messme.domain.type.None

interface NotificationsRemote {

    fun sendSosMessage(
        fromId: Long,
        toId: Long,
        token: String,
        message: String
    ): Either<Failure, None>

}