package com.serhiymysyshyn.messme.data.messages

import com.serhiymysyshyn.messme.domain.messages.MessageEntity
import com.serhiymysyshyn.messme.domain.type.Either
import com.serhiymysyshyn.messme.domain.type.Failure
import com.serhiymysyshyn.messme.domain.type.None

interface MessagesRemote {

    fun getChats(userId: Long, token: String): Either<Failure, List<MessageEntity>>

    fun getMessagesWithContact(contactId: Long, userId: Long, token: String): Either<Failure, List<MessageEntity>>

    fun sendMessage(
        fromId: Long,
        toId: Long,
        token: String,
        message: String,
        image: String
    ): Either<Failure, None>

    fun deleteMessagesByUser(userId: Long, messageId: Long, token: String): Either<Failure, None>

    fun deleteChat(senderId: Int, receiverId: Int): Either<Failure, None>
}