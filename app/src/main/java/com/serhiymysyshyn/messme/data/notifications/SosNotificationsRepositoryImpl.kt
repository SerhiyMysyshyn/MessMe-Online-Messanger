package com.serhiymysyshyn.messme.data.notifications

import com.serhiymysyshyn.messme.data.account.AccountCache
import com.serhiymysyshyn.messme.domain.sosNotification.SosNotificationRepository
import com.serhiymysyshyn.messme.domain.type.Either
import com.serhiymysyshyn.messme.domain.type.Failure
import com.serhiymysyshyn.messme.domain.type.None
import com.serhiymysyshyn.messme.domain.type.flatMap
import javax.inject.Inject

class SosNotificationsRepositoryImpl @Inject constructor(
    private val notificationsRemote: NotificationsRemote,
    private val accountCache: AccountCache
): SosNotificationRepository {

    override fun sendSosNotification(toId: Long, message: String): Either<Failure, None> {
        return accountCache.getCurrentAccount().flatMap {
            notificationsRemote.sendSosMessage(it.id, toId, it.token, message)
        }
    }
}