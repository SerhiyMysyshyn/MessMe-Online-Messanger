package com.serhiymysyshyn.messme.ui.home

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.serhiymysyshyn.messme.R
import com.serhiymysyshyn.messme.domain.friends.FriendEntity
import com.serhiymysyshyn.messme.domain.messages.MessageEntity
import com.serhiymysyshyn.messme.ui.core.navigation.Navigator
import com.serhiymysyshyn.messme.ui.friends.FriendsAdapter
import com.serhiymysyshyn.messme.ui.friends.FriendsFragment
import com.serhiymysyshyn.messme.ui.friends.SimpleFriendsAdapter
import kotlinx.android.synthetic.main.dialog_new_conversation.*

class AddNewConversationDialog(
    context: Context,
    cancelable: Boolean,
    friendsList: List<FriendEntity>?,
    chatsFragment: ChatsFragment,
    homeActivity: HomeActivity,
    navigator: Navigator,
    fragmentActivity: FragmentActivity
): AlertDialog(context) {
    private lateinit var friendsAdapter: SimpleFriendsAdapter
    private var friendsList: List<FriendEntity>?
    private var chatsFragment: ChatsFragment
    private var homeActivity: HomeActivity
    private val navigator: Navigator
    private val fragmentActivity: FragmentActivity

    init {
        setCancelable(cancelable)
        this.friendsList = friendsList
        this.chatsFragment = chatsFragment
        this.homeActivity = homeActivity
        this.navigator = navigator
        this.fragmentActivity = fragmentActivity

        friendsAdapter = SimpleFriendsAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_new_conversation)

        if (friendsList != null && friendsList!!.isNotEmpty()) {
            newConversationRv.visibility = View.VISIBLE
            emptyFriendListLayout.visibility = View.GONE

            newConversationRv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            newConversationRv.adapter = friendsAdapter
            friendsAdapter.submitList(friendsList)
        }else{
            newConversationRv.visibility = View.GONE
            emptyFriendListLayout.visibility = View.VISIBLE
        }

        friendsAdapter.setOnClick( click= { it, v ->
            (it as? FriendEntity)?.let {
                if (it.id != null) {
                    dismiss()
                    navigator.showChatWithContact(it.id, it.name, fragmentActivity)
                }
            }
        })


        closeNewConversationDialog.setOnClickListener {
            dismiss()
        }

        addNewFriendForConversation.setOnClickListener {
            dismiss()
            homeActivity.replaceFragment(FriendsFragment(homeActivity = homeActivity))
        }
    }


    override fun dismiss() {
        super.dismiss()
        chatsFragment.closeConversationDialog()
    }

    override fun cancel() {
        super.cancel()
    }


}