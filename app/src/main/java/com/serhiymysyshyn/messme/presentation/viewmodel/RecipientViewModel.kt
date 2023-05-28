package com.serhiymysyshyn.messme.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.serhiymysyshyn.messme.domain.friends.*
import com.serhiymysyshyn.messme.domain.sosNotification.SendSosNotification
import com.serhiymysyshyn.messme.domain.type.None
import javax.inject.Inject

class RecipientViewModel @Inject constructor(
    val getFriendsUseCase: GetFriends,
    val sendSosNotificationUseCase: SendSosNotification
) : BaseViewModel() {
    var friendsToRecipientsData: MutableLiveData<List<FriendEntity>> = MutableLiveData()
    var finishRecipientList: MutableLiveData<List<FriendEntity>> = MutableLiveData()



//    fun sendSosNotification(toId: Long, message: String) {
//        sendSosNotificationUseCase(SendSosNotification.Params(toId, message)) {
//            it.either(::handleFailure) {
//                handleSendSosNotification(it, toId)
//            }
//        }
//    }
//
//    private fun handleSendSosNotification(none: None, contactId: Long) {
//        println(">>> Sos notification was sent!")
//    }


    fun getFriends(needFetch: Boolean = false) {
        getFriendsUseCase(needFetch) { it.either(::handleFailure) { handleFriends(it, !needFetch) } }
    }

    private fun handleFriends(friends: List<FriendEntity>, fromCache: Boolean) {
        friendsToRecipientsData.value = friends

        if (fromCache) {
            getFriends(true)
        }
    }

    fun updateFinishRecipientList(list: List<FriendEntity>){
        finishRecipientList.value = list
    }

    override fun onCleared() {
        super.onCleared()
        getFriendsUseCase.unsubscribe()
    }
}