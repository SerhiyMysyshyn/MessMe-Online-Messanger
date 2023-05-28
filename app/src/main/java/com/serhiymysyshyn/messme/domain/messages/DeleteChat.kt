package com.serhiymysyshyn.messme.domain.messages

import com.serhiymysyshyn.messme.domain.interactor.UseCase
import com.serhiymysyshyn.messme.domain.type.Either
import com.serhiymysyshyn.messme.domain.type.Failure
import com.serhiymysyshyn.messme.domain.type.None
import javax.inject.Inject


class DeleteChat @Inject constructor(
    private val messagesRepository: MessagesRepository
): UseCase<None, DeleteChat.Params>(){

    override suspend fun run(params: DeleteChat.Params) = messagesRepository.deleteChat(params.senderId, params.receiverId)

    data class Params(val senderId: Int, val receiverId: Int)
}