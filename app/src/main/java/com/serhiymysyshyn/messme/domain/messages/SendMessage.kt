package com.serhiymysyshyn.messme.domain.messages

import com.serhiymysyshyn.messme.domain.interactor.UseCase
import com.serhiymysyshyn.messme.domain.type.None
import javax.inject.Inject

class SendMessage @Inject constructor(
    private val messagesRepository: MessagesRepository
) : UseCase<None, SendMessage.Params>() {

    override suspend fun run(params: Params) =
        messagesRepository.sendMessage(params.toId, params.message, params.image)

    data class Params(val toId: Long, val message: String, val image: String)
}