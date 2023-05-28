package com.serhiymysyshyn.messme.remote.messages

import com.serhiymysyshyn.messme.domain.messages.MessageEntity
import com.serhiymysyshyn.messme.remote.core.BaseResponse

class GetMessagesResponse (
    success: Int,
    message: String,
    val messages: List<MessageEntity>
) : BaseResponse(success, message)