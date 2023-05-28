package com.serhiymysyshyn.messme.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.serhiymysyshyn.messme.domain.messages.*
import com.serhiymysyshyn.messme.domain.type.None
import com.serhiymysyshyn.messme.ui.home.ChatsFragment
import com.serhiymysyshyn.messme.ui.home.HomeActivity
import javax.inject.Inject
import kotlin.concurrent.thread

class MessagesViewModel @Inject constructor(
    val getChatsUseCase: GetChats,
    val getMessagesUseCase: GetMessagesWithContact,
    val sendMessageUseCase: SendMessage,
    val deleteMessageUseCase: DeleteMessage,
    val deleteChatUseCase: DeleteChat
): BaseViewModel() {
    val getChatsData: MutableLiveData<List<MessageEntity>> = MutableLiveData()
    val getMessagesData: MutableLiveData<List<MessageEntity>> = MutableLiveData()
    val sendMessageData: MutableLiveData<None> = MutableLiveData()
    val deleteMessageData: MutableLiveData<None> = MutableLiveData()

    fun getChats(needFetch: Boolean = false) {
        getChatsUseCase(GetChats.Params(needFetch)) {
            it.either(::handleFailure) {
                handleGetChats(messages =  it, fromCache =  !needFetch)
            }
        }
    }



    fun getMessages(contactId: Long, needFetch: Boolean = false) {
        getMessagesUseCase(GetMessagesWithContact.Params(contactId, needFetch)) {
            it.either(::handleFailure) {
                handleGetMessages(it, contactId, !needFetch)
            }
        }
    }

    fun sendMessage(toId: Long, message: String, image: String) {
        sendMessageUseCase(SendMessage.Params(toId, message, image)) {
            it.either(::handleFailure) {
                handleSendMessage(it, toId)
            }
        }
    }

    fun deleteMessage(contactId: Long, messageId: Long) {
        deleteMessageUseCase(DeleteMessage.Params(messageId)) { it.either(::handleFailure) {handleDeleteMessage(contactId, it)}}
    }

    fun deleteChat(senderId: Int,
                   receiverId: Int,
                   homeActivity: HomeActivity,
                   messagesViewModel: MessagesViewModel){
        deleteChatUseCase(DeleteChat.Params(senderId, receiverId)) {
            if (it.isRight){
                messagesViewModel.getChats()
                homeActivity.replaceFragment(ChatsFragment(homeActivity))
            }else{

            }
        }
    }

    private fun handleGetChats(messages: List<MessageEntity>, fromCache: Boolean) {
        getChatsData.value = messages
        updateProgress(false)

        if (fromCache) {
            updateProgress(true)
            getChats(needFetch = true)
        }
    }

    private fun handleGetMessages(messages: List<MessageEntity>, contactId: Long, fromCache: Boolean) {
        getMessagesData.value = messages
        updateProgress(false)

        if (fromCache) {
            updateProgress(true)
            getMessages(contactId, true)
        }
    }

    private fun handleSendMessage(none: None, contactId: Long) {
        sendMessageData.value = none

        getMessages(contactId, true)
    }

    private fun handleDeleteMessage(contactId: Long, none: None?) {
        deleteMessageData.value = none

        getMessages(contactId, true)
    }


    override fun onCleared() {
        super.onCleared()
        getChatsUseCase.unsubscribe()
        getMessagesUseCase.unsubscribe()
        sendMessageUseCase.unsubscribe()
        deleteMessageUseCase.unsubscribe()
        deleteChatUseCase.unsubscribe()
    }
}