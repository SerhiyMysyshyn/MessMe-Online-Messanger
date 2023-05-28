package com.serhiymysyshyn.messme.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.serhiymysyshyn.messme.R
import com.serhiymysyshyn.messme.ui.settings.SettingFragment
import com.serhiymysyshyn.messme.databinding.ActivityNavigationBinding
import com.serhiymysyshyn.messme.domain.account.AccountEntity
import com.serhiymysyshyn.messme.domain.friends.FriendEntity
import com.serhiymysyshyn.messme.domain.type.Failure
import com.serhiymysyshyn.messme.domain.type.None
import com.serhiymysyshyn.messme.presentation.viewmodel.AccountViewModel
import com.serhiymysyshyn.messme.presentation.viewmodel.FriendsViewModel
import com.serhiymysyshyn.messme.remote.service.ApiService
import com.serhiymysyshyn.messme.ui.App
import com.serhiymysyshyn.messme.ui.core.BaseActivity
import com.serhiymysyshyn.messme.ui.core.BaseFragment
import com.serhiymysyshyn.messme.ui.core.ext.onFailure
import com.serhiymysyshyn.messme.ui.core.ext.onSuccess
import com.serhiymysyshyn.messme.ui.firebase.NotificationHelper
import com.serhiymysyshyn.messme.ui.friends.FriendRequestsFragment
import com.serhiymysyshyn.messme.ui.friends.FriendsFragment
import javax.inject.Inject


class HomeActivity : BaseActivity() {

    override var fragment: BaseFragment = ChatsFragment(this)

    override val contentId = R.layout.activity_navigation

    private lateinit var accountViewModel: AccountViewModel

    private lateinit var binding: ActivityNavigationBinding

    @Inject
    lateinit var friendsViewModel: FriendsViewModel


    override fun setupContent() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_navigation)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        App.appComponent.inject(this)

        accountViewModel = viewModel {
            onSuccess(accountData, ::handleAccount)
            onSuccess(logoutData, ::handleLogout)
            onFailure(failureData, ::handleFailure)
        }

        friendsViewModel = viewModel {
            onSuccess(addFriendData, ::handleAddFriend)
            onSuccess(friendRequestsData, ::handleFriendRequests)
            onFailure(failureData, ::handleFailure)
        }

        supportActionBar?.setHomeAsUpIndicator(R.drawable.menu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager.beginTransaction()
            .replace(R.id.requestContainer, FriendRequestsFragment()).commit()

        val type: String? = intent.getStringExtra("type")
        when (type) {
            NotificationHelper.TYPE_ADD_FRIEND -> {
                openDrawer()
                friendsViewModel.getFriendRequests()
                binding.navigation.requestContainer.visibility = View.VISIBLE
            }
            NotificationHelper.TYPE_SEND_MESSAGE -> {
                val contactId = intent.getLongExtra(ApiService.PARAM_CONTACT_ID, 0L)
                val contactName = intent.getStringExtra(ApiService.PARAM_NAME)
                navigator.showChatWithContact(contactId, contactName, this)
            }
        }

        binding.navigation.btnLogout.setOnClickListener {
            accountViewModel.logout()
        }

        binding.navigation.btnChats.setOnClickListener {
            replaceFragment(ChatsFragment(this))
            closeDrawer()
        }

        binding.navigation.btnAddFriend.setOnClickListener {
            if (binding.navigation.containerAddFriend.visibility == View.VISIBLE) {
                binding.navigation.containerAddFriend.visibility = View.GONE
            } else {
                binding.navigation.containerAddFriend.visibility = View.VISIBLE
            }
        }

        binding.navigation.btnSettings.setOnClickListener {
            replaceFragment(SettingFragment())
            closeDrawer()
        }

        binding.navigation.btnAdd.setOnClickListener {
            hideSoftKeyboard()
            showProgress()
            friendsViewModel.addFriend(binding.navigation.etEmail.text.toString())
        }

        binding.navigation.btnFriends.setOnClickListener {
            replaceFragment(FriendsFragment(this))
            closeDrawer()
        }

        binding.navigation.btnRequests.setOnClickListener {
            friendsViewModel.getFriendRequests(true)

            if (binding.navigation.requestContainer.visibility == View.VISIBLE) {
                binding.navigation.requestContainer.visibility = View.GONE
            } else {
                binding.navigation.requestContainer.visibility = View.VISIBLE
            }
        }

        binding.navigation.profileContainer.setOnClickListener {
            navigator.showAccount(this)
            Handler(Looper.getMainLooper()).postDelayed({
                closeDrawer()
            }, 200)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                if (binding.drawerLayout.isDrawerOpen(binding.navigation.navigationView)) {
                    binding.drawerLayout.closeDrawer(binding.navigation.navigationView)
                } else {
                    binding.drawerLayout.openDrawer(binding.navigation.navigationView)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openDrawer() {
        hideSoftKeyboard()
        binding.drawerLayout.openDrawer(binding.navigation.navigationView)
    }

    private fun closeDrawer(animate: Boolean = true) {
        hideSoftKeyboard()
        binding.drawerLayout.closeDrawer(binding.navigation.navigationView, animate)
    }

    override fun onResume() {
        super.onResume()
        accountViewModel.getAccount()
        accountViewModel.updateLastSeen()
    }

    private fun handleAccount(accountEntity: AccountEntity?) {
        accountEntity?.let {
            binding.navigation.account = it
        }
    }

    private fun handleLogout(none: None?) {
        navigator.showLogin(this)
        finish()
    }

    private fun handleAddFriend(none: None?) {
        binding.navigation.etEmail.text.clear()
        binding.navigation.containerAddFriend.visibility = View.GONE

        hideProgress()
        showMessage("Запит відправлений.")
    }

    private fun handleFriendRequests(requests: List<FriendEntity>?) {
        if (requests?.isEmpty() == true) {
            binding.navigation.requestContainer.visibility = View.GONE
            if (binding.drawerLayout.isDrawerOpen(binding.navigation.navigationView)) {
                showMessage("Немає вхідних запрошень.")
            }
        }
    }

    override fun handleFailure(failure: Failure?) {
        hideProgress()
        when (failure) {
            Failure.ContactNotFoundError -> navigator.showEmailNotFoundDialog(
                this,
                binding.navigation.etEmail.text.toString()
            )
            else -> super.handleFailure(failure)
        }
    }


    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(binding.navigation.navigationView)) {
            hideSoftKeyboard()
            binding.drawerLayout.closeDrawer(binding.navigation.navigationView)
        } else {
            super.onBackPressed()
        }
    }
}
