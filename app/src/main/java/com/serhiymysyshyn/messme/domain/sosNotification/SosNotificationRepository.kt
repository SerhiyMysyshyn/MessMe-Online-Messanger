package com.serhiymysyshyn.messme.domain.sosNotification

import com.serhiymysyshyn.messme.domain.type.Either
import com.serhiymysyshyn.messme.domain.type.Failure
import com.serhiymysyshyn.messme.domain.type.None

interface SosNotificationRepository {

    fun sendSosNotification(
        toId: Long,
        message: String
    ): Either<Failure, None>

}