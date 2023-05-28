package com.serhiymysyshyn.messme.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.serhiymysyshyn.messme.R
import com.serhiymysyshyn.messme.cache.ChatDatabase
import com.serhiymysyshyn.messme.domain.friends.FriendEntity
import com.serhiymysyshyn.messme.domain.messages.MessageEntity
import com.serhiymysyshyn.messme.presentation.viewmodel.FriendsViewModel
import com.serhiymysyshyn.messme.presentation.viewmodel.MessagesViewModel
import com.serhiymysyshyn.messme.ui.App
import com.serhiymysyshyn.messme.ui.core.BaseFragment
import com.serhiymysyshyn.messme.ui.core.BaseListFragment
import com.serhiymysyshyn.messme.ui.core.ext.onFailure
import com.serhiymysyshyn.messme.ui.core.ext.onSuccess
import com.serhiymysyshyn.messme.ui.core.inTransaction
import com.serhiymysyshyn.messme.ui.core.navigation.Navigator
import com.serhiymysyshyn.messme.ui.friends.FriendsAdapter
import com.serhiymysyshyn.messme.ui.friends.FriendsFragment
import kotlinx.android.synthetic.main.fragment_list.*


class ChatsFragment(val homeActivity: HomeActivity) : BaseListFragment() {
    override val viewAdapter = ChatsAdapter()

    override val titleToolbar = R.string.chats

    private var isConversationDialogVisible: Boolean = false

    private lateinit var messagesViewModel: MessagesViewModel
    private lateinit var friendsViewModel: FriendsViewModel
    private var requestCounter: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        messagesViewModel = viewModel {
            onSuccess(progressData, ::updateProgress)
            onFailure(failureData, ::handleFailure)
        }

        friendsViewModel = viewModel {
            onSuccess(friendsData, ::handleFriends)
            //onSuccess(deleteFriendData, ::handleDeleteFriend)
            onSuccess(progressData, ::updateProgress)
            onFailure(failureData, ::handleFailure)
        }

//--------------------------------------------------------------------------------------------------
        setHelpButtonClickListener(View.OnClickListener {
            friendsViewModel.getFriends(false)
        })
        setHelpButtonImageResource(R.drawable.ic_action_add)
//--------------------------------------------------------------------------------------------------

        viewAdapter.setOnClick( click= { it, v ->
            (it as? MessageEntity)?.let {
                val contact = it.contact
                if (contact != null) {
                    navigator.showChatWithContact(contact.id, contact.name, requireActivity())
                }
            }
        }, longClick = { it, v ->
            (it as? MessageEntity)?.let {
                navigator.showDeleteChatDialog(requireContext()) {
                    (it as? MessageEntity)?.let {
                    messagesViewModel.deleteChat(
                        senderId = it.senderId.toInt(),
                        receiverId = it.receiverId.toInt(),
                        homeActivity = homeActivity!!,
                        messagesViewModel = messagesViewModel)
                    }
                }
            }
        })



        ChatDatabase.getInstance(requireContext()).messagesDao.getLiveChats().observe(this, Observer {
            val list = it.distinctBy { it.contact?.id }.toList()
            handleChats(list)
        })
    }

    override fun onResume() {
        super.onResume()
        messagesViewModel.getChats()
    }

    fun handleChats(messages: List<MessageEntity>?) {
        if (messages != null && messages.isNotEmpty()) {
            emptyListContainer.visibility = View.GONE

            viewAdapter.submitList(messages)
            homeActivity.replaceFragment(this)
        }else{
            emptyListContainer.visibility = View.VISIBLE
            emptyListContainerText.setText(R.string.chats_is_empty)

        }

    }




    private fun handleFriends(friends: List<FriendEntity>?) {
        if(friends?.size == 0 || friends == null){
            while (requestCounter < 3){
                friendsViewModel.getFriends(false)
                requestCounter += 1
            }
            Toast.makeText(context, R.string.friend_list_empty, Toast.LENGTH_LONG).show()
        }else{
            hideProgress()
            requestCounter = 0
            if (!isConversationDialogVisible){
                try {
                    isConversationDialogVisible = true
                    navigator.showNewConversationDialog(context, friends, this, homeActivity, requireActivity())
                }catch (e: java.lang.Exception){
                    println(">>> Error: $e")
                    Toast.makeText(context, "Something went wrong :( Please, try again", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun closeConversationDialog() {
        isConversationDialogVisible = false
    }
}
