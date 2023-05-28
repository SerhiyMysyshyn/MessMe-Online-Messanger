package com.serhiymysyshyn.messme.data.messages

import com.serhiymysyshyn.messme.domain.messages.MessageEntity

interface MessagesCache {
    fun saveMessage(entity: MessageEntity)

    fun saveMessages(entities: List<MessageEntity>)

    fun getChats(): List<MessageEntity>

    fun getMessagesWithContact(contactId: Long): List<MessageEntity>

    fun deleteMessagesByUser(messageId: Long)

    fun deleteChat(senderId: Int, receiverId: Int)
}