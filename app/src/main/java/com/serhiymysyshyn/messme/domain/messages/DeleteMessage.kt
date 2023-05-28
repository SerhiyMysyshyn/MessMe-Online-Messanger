package com.serhiymysyshyn.messme.domain.messages

import com.serhiymysyshyn.messme.domain.interactor.UseCase
import com.serhiymysyshyn.messme.domain.type.None
import javax.inject.Inject

class DeleteMessage @Inject constructor(
    private val messagesRepository: MessagesRepository
) : UseCase<None, DeleteMessage.Params>() {

    override suspend fun run(params: Params) = messagesRepository.deleteMessagesByUser(params.messageId)

    data class Params(val messageId: Long)
}