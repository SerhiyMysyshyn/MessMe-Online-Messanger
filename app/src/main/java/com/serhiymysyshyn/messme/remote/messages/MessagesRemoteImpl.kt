package com.serhiymysyshyn.messme.remote.messages

import com.serhiymysyshyn.messme.data.messages.MessagesRemote
import com.serhiymysyshyn.messme.domain.messages.MessageEntity
import com.serhiymysyshyn.messme.domain.type.Either
import com.serhiymysyshyn.messme.domain.type.Failure
import com.serhiymysyshyn.messme.domain.type.None
import com.serhiymysyshyn.messme.remote.core.Request
import com.serhiymysyshyn.messme.remote.service.ApiService
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

class MessagesRemoteImpl @Inject constructor(
    private val request: Request,
    private val service: ApiService
) : MessagesRemote {

    override fun getChats(userId: Long, token: String): Either<Failure, List<MessageEntity>> {
        return request.make(service.getLastMessages(createGetLastMessagesMap(userId, token))) { it.messages }
    }

    override fun getMessagesWithContact(
        contactId: Long,
        userId: Long,
        token: String
    ): Either<Failure, List<MessageEntity>> {
        return request.make(service.getMessagesWithContact(
            createGetMessagesWithContactMap(contactId, userId,token))
        ) { it.messages }
    }

    override fun sendMessage(
        fromId: Long,
        toId: Long,
        token: String,
        message: String,
        image: String
    ): Either<Failure, None> {
        return request.make(service.sendMessage(
            createSendMessageMap(fromId, toId, token, message, image))) { None() }
    }

    override fun deleteMessagesByUser(userId: Long, messageId: Long, token: String): Either<Failure, None> {
        return request.make(service.deleteMessagesByUser(
            createDeleteMessagesMap(userId, messageId, token))) { None() }
    }

    override fun deleteChat(senderId: Int, receiverId: Int): Either<Failure, None> {
        return request.make(service.deleteChat(createDeleteChatMap(senderId, receiverId))) { None() }
    }



    private fun createDeleteChatMap(
        senderId: Int,
        receiverId: Int
    ): Map<String, String> {

        val map = HashMap<String, String>()
        map.put(ApiService.PARAM_SENDER_ID, senderId.toString())
        map.put(ApiService.PARAM_RECEIVER_ID, receiverId.toString())

        return map
    }


    private fun createGetLastMessagesMap(
        userId: Long,
        token: String
    ): Map<String, String> {
        val map = HashMap<String, String>()
        map.put(ApiService.PARAM_USER_ID, userId.toString())
        map.put(ApiService.PARAM_TOKEN, token)

        return map
    }

    private fun createGetMessagesWithContactMap(
        contactId: Long,
        userId: Long,
        token: String
    ): Map<String, String> {
        val map = HashMap<String, String>()
        map.put(ApiService.PARAM_CONTACT_ID, contactId.toString())
        map.put(ApiService.PARAM_USER_ID, userId.toString())
        map.put(ApiService.PARAM_TOKEN, token)

        return map
    }

    private fun createSendMessageMap(
        fromId: Long,
        toId: Long,
        token: String,
        message: String,
        image: String
    ): Map<String, String> {
        val date = Date().time
        var type = 1

        val map = HashMap<String, String>()

        if (image.isNotBlank()) {
            type = 2
            map.put(ApiService.PARAM_IMAGE_NEW, image)
            map.put(ApiService.PARAM_IMAGE_NEW_NAME, "user_${fromId}_${date}_photo")
        }

        map.put(ApiService.PARAM_SENDER_ID, fromId.toString())
        map.put(ApiService.PARAM_RECEIVER_ID, toId.toString())
        map.put(ApiService.PARAM_TOKEN, token)
        map.put(ApiService.PARAM_MESSAGE, message)
        map.put(ApiService.PARAM_MESSAGE_TYPE, type.toString())
        map.put(ApiService.PARAM_MESSAGE_DATE, date.toString())

        return map
    }

    private fun createDeleteMessagesMap(
        userId: Long,
        messageId: Long,
        token: String
    ): Map<String, String> {
        val itemsArrayObject = JSONObject()
        val itemsArray = JSONArray()
        val itemObject = JSONObject()

        itemObject.put("message_id", messageId)
        itemsArray.put(itemObject)
        itemsArrayObject.put("messages", itemsArray)


        val map = HashMap<String, String>()
        map.put(ApiService.PARAM_USER_ID, userId.toString())
        map.put(ApiService.PARAM_MESSAGES_IDS, itemsArrayObject.toString())
        map.put(ApiService.PARAM_TOKEN, token)

        return map
    }
}