package com.serhiymysyshyn.messme.domain.sosNotification

import android.location.Location
import com.serhiymysyshyn.messme.domain.interactor.UseCase
import com.serhiymysyshyn.messme.domain.type.None
import javax.inject.Inject

class SendSosNotification @Inject constructor(
    private val sosNotificationRepository: SosNotificationRepository
) : UseCase<None, SendSosNotification.Params>() {

    override suspend fun run(params: Params) =
        sosNotificationRepository.sendSosNotification(params.toId, params.message)

    data class Params(val toId: Long, val message: String)
}