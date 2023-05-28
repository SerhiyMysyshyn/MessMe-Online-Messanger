package com.serhiymysyshyn.messme.ui.friends

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.serhiymysyshyn.messme.R
import com.serhiymysyshyn.messme.domain.friends.FriendEntity
import com.serhiymysyshyn.messme.domain.type.None
import com.serhiymysyshyn.messme.presentation.viewmodel.FriendsViewModel
import com.serhiymysyshyn.messme.ui.App
import com.serhiymysyshyn.messme.ui.core.BaseListFragment
import com.serhiymysyshyn.messme.ui.core.ext.onFailure
import com.serhiymysyshyn.messme.ui.core.ext.onSuccess
import com.serhiymysyshyn.messme.ui.home.HomeActivity
import kotlinx.android.synthetic.main.fragment_list.*

class FriendsFragment(val homeActivity: HomeActivity) : BaseListFragment() {
    override val viewAdapter = FriendsAdapter()

    lateinit var friendsViewModel: FriendsViewModel

    override val titleToolbar = R.string.screen_friends

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        friendsViewModel = viewModel {
            onSuccess(friendsData, ::handleFriends)
            onSuccess(deleteFriendData, ::handleDeleteFriend)
            onSuccess(progressData, ::updateProgress)
            onFailure(failureData, ::handleFailure)
            onSuccess(addFriendData, ::handleAddFriend)
        }




        setHelpButtonClickListener(View.OnClickListener {
            navigator.showAddNewFriendDialog(context, friendsViewModel)
        })

        setHelpButtonImageResource(R.drawable.ic_action_add_new_friend)


        setOnItemClickListener { it, v ->
            (it as? FriendEntity)?.let {
                when (v.id) {
                    R.id.btnRemove -> showDeleteFriendDialog(it)
                    else -> {
                        navigator.showUser(requireActivity(), it)
                    }
                }
            }
        }

        friendsViewModel.getFriends()
    }


    private fun showDeleteFriendDialog(entity: FriendEntity) {
        activity?.let {
            AlertDialog.Builder(it)
                .setMessage(getString(R.string.delete_friend))

                .setPositiveButton(android.R.string.yes) { dialog, which ->
                    friendsViewModel.deleteFriend(friendEntity = entity, homeActivity = homeActivity, friendsViewModel = friendsViewModel)
                }

                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
        }
    }


    private fun handleFriends(friends: List<FriendEntity>?) {
        hideProgress()
        if (friends != null && friends.isNotEmpty()) {
            emptyListContainer.visibility = View.GONE

            viewAdapter.submitList(friends)
        }else{
            emptyListContainer.visibility = View.VISIBLE
            emptyListContainerText.setText(R.string.your_friends_list_is_empty)
        }
    }

    private fun handleDeleteFriend(none: None?) {
        friendsViewModel.getFriends()
    }

    private fun handleAddFriend(none: None?) {
        hideProgress()
        showMessage("Запит відправлений.")
    }
}