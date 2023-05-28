package com.serhiymysyshyn.messme.remote.notifications

import com.serhiymysyshyn.messme.data.notifications.NotificationsRemote
import com.serhiymysyshyn.messme.domain.type.Either
import com.serhiymysyshyn.messme.domain.type.Failure
import com.serhiymysyshyn.messme.domain.type.None
import com.serhiymysyshyn.messme.remote.core.Request
import com.serhiymysyshyn.messme.remote.service.ApiService
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

class NotificationsRemoteImpl @Inject constructor(
    private val request: Request,
    private val service: ApiService
): NotificationsRemote {

    override fun sendSosMessage(
        fromId: Long,
        toId: Long,
        token: String,
        message: String
    ): Either<Failure, None> {
        //return request.make(service.sendSosNotification(createSendMessageMap(fromId, toId, token, message))) { None() }
        return request.make(service.sendMessage(createSendMessageMap(fromId, toId, token, message))) { None() }
    }

    private fun createSendMessageMap(
        fromId: Long,
        toId: Long,
        token: String,
        message: String
    ): Map<String, String> {
        val date = Date().time
        var type = 3

        val map = HashMap<String, String>()

        map.put(ApiService.PARAM_SENDER_ID, fromId.toString())
        map.put(ApiService.PARAM_RECEIVER_ID, toId.toString())
        map.put(ApiService.PARAM_TOKEN, token)
        map.put(ApiService.PARAM_MESSAGE, message)
        map.put(ApiService.PARAM_MESSAGE_TYPE, type.toString())
        map.put(ApiService.PARAM_MESSAGE_DATE, date.toString())

        return map
    }

//    private fun createSendMessageMap(
//        fromId: Long,
//        toId: Long,
//        token: String,
//        message: String
//    ): Map<String, String> {
//        val date = Date().time
//
//        val map = HashMap<String, String>()
//
//        map.put(ApiService.PARAM_SENDER_ID, fromId.toString())
//        map.put(ApiService.PARAM_RECEIVER_ID, toId.toString())
//        map.put(ApiService.PARAM_TOKEN, token)
//        map.put(ApiService.PARAM_MESSAGE, message)
//        map.put(ApiService.PARAM_MESSAGE_DATE, date.toString())
//
//        return map
//    }
}