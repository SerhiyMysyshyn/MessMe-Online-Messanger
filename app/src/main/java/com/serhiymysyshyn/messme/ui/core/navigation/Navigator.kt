package com.serhiymysyshyn.messme.ui.core.navigation

import android.app.ActionBar.LayoutParams
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.serhiymysyshyn.messme.R
import com.serhiymysyshyn.messme.cache.SharedPrefsManager
import com.serhiymysyshyn.messme.domain.friends.FriendEntity
import com.serhiymysyshyn.messme.domain.messages.MessageEntity
import com.serhiymysyshyn.messme.presentation.Authenticator
import com.serhiymysyshyn.messme.presentation.injection.CacheModule
import com.serhiymysyshyn.messme.presentation.viewmodel.FriendsViewModel
import com.serhiymysyshyn.messme.presentation.viewmodel.MediaViewModel
import com.serhiymysyshyn.messme.presentation.viewmodel.MessagesViewModel
import com.serhiymysyshyn.messme.remote.service.ApiService
import com.serhiymysyshyn.messme.ui.account.AccountActivity
import com.serhiymysyshyn.messme.ui.core.BaseActivity
import com.serhiymysyshyn.messme.ui.core.PermissionManager
import com.serhiymysyshyn.messme.ui.friends.AddNewFriendDialog
import com.serhiymysyshyn.messme.ui.home.AddNewConversationDialog
import com.serhiymysyshyn.messme.ui.home.ChatsFragment
import com.serhiymysyshyn.messme.ui.home.HomeActivity
import com.serhiymysyshyn.messme.ui.home.MessagesActivity
import com.serhiymysyshyn.messme.ui.login.EnterPinCodeActivity
import com.serhiymysyshyn.messme.ui.login.ForgetPasswordActivity
import com.serhiymysyshyn.messme.ui.login.LoginActivity
import com.serhiymysyshyn.messme.ui.register.RegisterActivity
import com.serhiymysyshyn.messme.ui.user.UserActivity
import kotlinx.android.synthetic.main.activity_pin_application.*
import kotlinx.android.synthetic.main.dialog_image.view.*
import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class Navigator
@Inject constructor(
    private val authenticator: Authenticator,
    private val permissionManager: PermissionManager
) {
    val cacheModule: CacheModule = CacheModule()
    private lateinit var sharedPrefsManager: SharedPrefsManager

    fun showMain(context: Context) {
        authenticator.userLoggedIn {
            if (it) {
                sharedPrefsManager = SharedPrefsManager(cacheModule.provideSharedPreferences(context!!))
                if (sharedPrefsManager.isPinAppActivated()){
                    showEnterPinActivity(context, false)
                }else{
                    showHome(context, false)
                }
            } else {
                showLogin(context, false)
            }
        }
    }

    fun showEnterPinActivity(context: Context, newTask: Boolean = true) = context.startActivity<EnterPinCodeActivity>(newTask = newTask)

    fun showLogin(context: Context, newTask: Boolean = true) = context.startActivity<LoginActivity>(newTask = newTask)
    fun showSignUp(context: Context) = context.startActivity<RegisterActivity>()

    fun showForgedPassword(context: Context) = context.startActivity<ForgetPasswordActivity>()

    fun showHome(context: Context, newTask: Boolean = true) = context.startActivity<HomeActivity>(newTask = newTask)

// Add new friend dialog
    fun showAddNewFriendDialog(context: Context?, friendsViewModel: FriendsViewModel){
        val addNewFriendDialog = AddNewFriendDialog(context!!, true, friendsViewModel)
        addNewFriendDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        addNewFriendDialog.window?.attributes?.width = LayoutParams.MATCH_PARENT
        addNewFriendDialog.show()
    }

// Create new conversation dialog
    fun showNewConversationDialog(context: Context?, friendsList: List<FriendEntity>?, chatsFragment: ChatsFragment, homeActivity: HomeActivity, fragmentActivity: FragmentActivity){
        val addNewConversationDialog = AddNewConversationDialog(
            context!!,
            true,
            friendsList,
            chatsFragment,
            homeActivity,
            this,
        fragmentActivity)
        addNewConversationDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        addNewConversationDialog.window?.attributes?.width = LayoutParams.MATCH_PARENT
        addNewConversationDialog.show()
    }


    fun showEmailNotFoundDialog(context: Context, email: String) {
        AlertDialog.Builder(context)
            .setMessage(context.getString(R.string.message_promt_app))

            .setPositiveButton(android.R.string.yes) { dialog, which ->
                showEmailInvite(context, email)
            }

            .setNegativeButton(android.R.string.no, null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

    private fun showEmailInvite(context: Context, email: String) {
        val appPackageName = context.packageName
        val emailIntent = Intent(
            Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", email, null
            )
        )
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, context.resources.getString(R.string.message_subject_promt_app))
        emailIntent.putExtra(
            Intent.EXTRA_TEXT, context.resources.getString(R.string.message_text_promt_app) + " "
                    + context.resources.getString(R.string.url_google_play) + appPackageName
        )
        context.startActivity(Intent.createChooser(emailIntent, "Надіслати"))
    }


    fun showAccount(context: Context) {
        context.startActivity<AccountActivity>()
    }


    fun showUser(context: Context, friendEntity: FriendEntity) {
        val bundle = Bundle()
        bundle.putString(ApiService.PARAM_IMAGE, friendEntity.image)
        bundle.putString(ApiService.PARAM_NAME, friendEntity.name)
        bundle.putString(ApiService.PARAM_EMAIL, friendEntity.email)
        bundle.putString(ApiService.PARAM_STATUS, friendEntity.status)
        bundle.putLong(ApiService.PARAM_CONTACT_ID, friendEntity.id)
        context.startActivity<UserActivity>(args = bundle)
    }

    fun showChatWithContact(contactId: Long, contactName: String, context: Context) {
        val bundle = Bundle()
        bundle.putLong(ApiService.PARAM_CONTACT_ID, contactId)
        bundle.putString(ApiService.PARAM_NAME, contactName)
        context.startActivity<MessagesActivity>(args = bundle)
    }


    fun showPickFromDialog(activity: AppCompatActivity, onPick: (fromCamera: Boolean) -> Unit) {
        val options = arrayOf<CharSequence>(
            activity.getString(R.string.camera),
            activity.getString(R.string.gallery)
        )

        val builder = AlertDialog.Builder(activity)

        builder.setTitle(activity.getString(R.string.pick))
        builder.setItems(options) { _, item ->
            when (options[item]) {
                activity.getString(R.string.camera) -> {
                    permissionManager.checkCameraPermission(activity) {
                        onPick(true)
                    }
                }
                activity.getString(R.string.gallery) -> {
                    permissionManager.checkWritePermission(activity) {
                        onPick(false)
                    }
                }
            }
        }
        builder.show()
    }

    fun showCamera(activity: AppCompatActivity, uri: Uri) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)

        activity.startActivityForResult(intent, MediaViewModel.CAPTURE_IMAGE_REQUEST_CODE)
    }

    fun showGallery(activity: AppCompatActivity) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"

        activity.startActivityForResult(intent, MediaViewModel.PICK_IMAGE_REQUEST_CODE)
    }

    fun showOptionsDialog(context: Context, mEntity: MessageEntity, messagesViewModel: MessagesViewModel, contactId: Long) {
        var aSize = 0
        var optionsList = Array(aSize){""}

        when(mEntity.type){
            1 -> {
                aSize = 1
                optionsList = Array(aSize){""}
                optionsList[0] = "Видалити повідомлення"
            }
            2 -> {
                aSize = 1
                optionsList = Array(aSize){""}
                optionsList[0] = "Видалити повідомлення"
            }
            3 -> {
                aSize = 2
                optionsList = Array(aSize){""}
                optionsList[0] = "Видалити повідомлення"
                optionsList[1] = "Відкрити в Google Maps"
            }
        }


        AlertDialog.Builder(context)
            //.setTitle("Виберіть дію")
            .setItems(optionsList) { dialog, which ->
                when(which){
                    0 -> showDeleteMessageDialog(context, { messagesViewModel.deleteMessage(contactId, mEntity.id) })
                    1 -> openGpsInMaps(context, mEntity)
                }
            }

            .setCancelable(true)
            .create().show()
    }

    fun showDeleteMessageDialog(context: Context, onPositive: () -> Unit) {
        AlertDialog.Builder(context)
            .setMessage(context.getString(R.string.remove_message))

            .setPositiveButton(android.R.string.yes) { dialog, which ->
                onPositive()
                dialog.dismiss()
            }

            .setNegativeButton(android.R.string.no, null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

    fun openGpsInMaps(context: Context, mEntity: MessageEntity){
        try {
            val s = mEntity.message.split("\n\n")[1]
            val ss = s.substring(17)
            val sss = ss.substring(0, ss.length - 1)
            val ssss = sss.split(",")

            val latitude: String = ssss[0]
            val longitude: String = ssss[1]

            var customUri = "google.streetview:cbll=${latitude},${longitude}"
            val gmmIntentUri = Uri.parse(customUri)
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            context.startActivity(mapIntent)
        }catch (e: java.lang.Exception){
            Toast.makeText(context, "Немає GPS даниї або вони не дійсні!", Toast.LENGTH_SHORT).show()
        }
    }

    fun showDeleteChatDialog(context: Context, onPositive: () -> Unit) {
        AlertDialog.Builder(context)
            .setMessage(context.getString(R.string.delete_chat))

            .setPositiveButton(android.R.string.yes) { dialog, which ->
                onPositive()
                dialog.dismiss()
            }

            .setNegativeButton(android.R.string.no, null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

    fun showImageDialog(context: Context, image: Drawable) {
        val view = LayoutInflater.from(context).inflate(
            R.layout.dialog_image,
            null
        )

        val dialog = Dialog(context, R.style.DialogFullscreen)

        view.imageView.setImageDrawable(image)
        dialog.setContentView(view)

        view.imageView.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }
}

private inline fun <reified T> Context.startActivity(newTask: Boolean = false, args: Bundle? = null) {
    this.startActivity(Intent(this, T::class.java).apply {
        if (newTask) {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        putExtra("args", args)
    })
}
