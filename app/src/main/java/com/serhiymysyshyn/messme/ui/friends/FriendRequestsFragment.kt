package com.serhiymysyshyn.messme.ui.friends

import android.os.Bundle
import android.view.View
import com.serhiymysyshyn.messme.R
import com.serhiymysyshyn.messme.domain.friends.FriendEntity
import com.serhiymysyshyn.messme.domain.type.None
import com.serhiymysyshyn.messme.presentation.viewmodel.FriendsViewModel
import com.serhiymysyshyn.messme.ui.App
import com.serhiymysyshyn.messme.ui.core.BaseListFragment
import com.serhiymysyshyn.messme.ui.core.ext.onFailure
import com.serhiymysyshyn.messme.ui.core.ext.onSuccess

class FriendRequestsFragment : BaseListFragment() {
    override val viewAdapter = FriendRequestsAdapter()

    override val layoutId = R.layout.inner_fragment_list

    lateinit var friendsViewModel: FriendsViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        base {
            friendsViewModel = viewModel {
                onSuccess(friendRequestsData, ::handleFriendRequests)
                onSuccess(approveFriendData, ::handleFriendRequestAction)
                onSuccess(cancelFriendData, ::handleFriendRequestAction)
                onFailure(failureData, ::handleFailure)
            }
        }


        setOnItemClickListener { item, v ->
            (item as? FriendEntity)?.let {
                when (v.id) {
                    R.id.btnApprove -> {
                        showProgress()
                        friendsViewModel.approveFriend(it)
                    }
                    R.id.btnCancel -> {
                        showProgress()
                        friendsViewModel.cancelFriend(it)
                    }
                    else -> {
                        activity?.let { act ->
                            navigator.showUser(act, it)
                        }
                    }
                }
            }
        }

        friendsViewModel.getFriendRequests()
    }


    private fun handleFriendRequests(requests: List<FriendEntity>?) {
        hideProgress()
        if (requests != null && requests.isNotEmpty()) {
            viewAdapter.submitList(requests)
        }
    }

    private fun handleFriendRequestAction(none: None?) {
        hideProgress()
        friendsViewModel.getFriendRequests()
    }
}