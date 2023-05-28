package com.serhiymysyshyn.messme.ui.friends

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Button
import com.andreabaccega.widget.FormEditText
import com.serhiymysyshyn.messme.R
import com.serhiymysyshyn.messme.presentation.viewmodel.FriendsViewModel

class AddNewFriendDialog(
    context: Context,
    cancelable: Boolean,
    friendsViewModel: FriendsViewModel
) : Dialog(context) {
    private lateinit var friendsViewModel: FriendsViewModel
    private lateinit var findNewFriendButton: Button
    private lateinit var inputEmail: FormEditText

    init {
        setCancelable(cancelable)
        this.friendsViewModel = friendsViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_add_friend)

        inputEmail = findViewById(R.id.diEmail)
        findNewFriendButton = findViewById<Button>(R.id.diAdd).apply {
            setOnClickListener{
                friendsViewModel.addFriend(inputEmail.text.toString())
                dismiss()
            }
        }
    }

    override fun dismiss() {
        super.dismiss()
    }

    override fun cancel() {
        super.cancel()
    }


}