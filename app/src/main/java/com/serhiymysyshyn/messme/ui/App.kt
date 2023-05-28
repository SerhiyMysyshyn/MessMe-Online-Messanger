package com.serhiymysyshyn.messme.ui

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import com.serhiymysyshyn.messme.R
import com.serhiymysyshyn.messme.domain.messages.SendMessage
import com.serhiymysyshyn.messme.domain.sosNotification.SendSosNotification
import com.serhiymysyshyn.messme.presentation.injection.AppModule
import com.serhiymysyshyn.messme.presentation.injection.CacheModule
import com.serhiymysyshyn.messme.presentation.injection.RemoteModule
import com.serhiymysyshyn.messme.presentation.injection.ViewModelModule
import com.serhiymysyshyn.messme.presentation.viewmodel.MessagesViewModel
import com.serhiymysyshyn.messme.ui.account.AccountActivity
import com.serhiymysyshyn.messme.ui.account.AccountFragment
import com.serhiymysyshyn.messme.ui.core.navigation.RouteActivity
import com.serhiymysyshyn.messme.ui.firebase.FirebaseService
import com.serhiymysyshyn.messme.ui.firebase.SosNotificationSender
import com.serhiymysyshyn.messme.ui.friends.FriendRequestsFragment
import com.serhiymysyshyn.messme.ui.friends.FriendsFragment
import com.serhiymysyshyn.messme.ui.home.ChatsFragment
import com.serhiymysyshyn.messme.ui.home.HomeActivity
import com.serhiymysyshyn.messme.ui.home.MessagesActivity
import com.serhiymysyshyn.messme.ui.home.MessagesFragment
import com.serhiymysyshyn.messme.ui.login.EnterPinCodeActivity
import com.serhiymysyshyn.messme.ui.login.ForgetPasswordFragment
import com.serhiymysyshyn.messme.ui.login.LoginFragment
import com.serhiymysyshyn.messme.ui.register.RegisterActivity
import com.serhiymysyshyn.messme.ui.register.RegisterFragment
import com.serhiymysyshyn.messme.ui.settings.SettingFragment
import com.serhiymysyshyn.messme.ui.settings.chats_settings.ChatSettingsActivity
import com.serhiymysyshyn.messme.ui.settings.pin_application.PinApplicationActivity
import com.serhiymysyshyn.messme.ui.settings.sos_notification.SosNotificationActivity
import com.serhiymysyshyn.messme.ui.user.UserActivity
import com.serhiymysyshyn.messme.ui.user.UserFragment
import dagger.Component
import javax.inject.Inject
import javax.inject.Singleton

class App : Application() {
    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        initAppComponent()
        createChannels()


    }

    private fun initAppComponent() {
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this)).build()
    }

    private fun createChannels() {
        var mManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val androidChannel = NotificationChannel(packageName, "$packageName", NotificationManager.IMPORTANCE_HIGH)
            androidChannel.enableLights(true)
            androidChannel.enableVibration(true)
            androidChannel.lightColor = Color.GREEN
            androidChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

            mManager.createNotificationChannel(androidChannel)
        }
    }

}

@Singleton
@Component(modules = [AppModule::class, CacheModule::class, RemoteModule::class, ViewModelModule::class])
interface AppComponent {

    //activities
    fun inject(activity: RegisterActivity)
    fun inject(activity: RouteActivity)
    fun inject(activity: HomeActivity)
    fun inject(activity: AccountActivity)
    fun inject(activity: MessagesActivity)
    fun inject(activity: UserActivity)
    fun inject(activity: SosNotificationActivity)
    fun inject(activity: PinApplicationActivity)
    fun inject(activity: ChatSettingsActivity)
    fun inject(activity: EnterPinCodeActivity)

    //fragments
    fun inject(fragment: RegisterFragment)
    fun inject(fragment: LoginFragment)
    fun inject(fragment: ChatsFragment)
    fun inject(fragment: FriendsFragment)
    fun inject(fragment: FriendRequestsFragment)
    fun inject(fragment: AccountFragment)
    fun inject(fragment: MessagesFragment)
    fun inject(fragment: UserFragment)
    fun inject(fragment: ForgetPasswordFragment)
    fun inject(fragment: SettingFragment)

    //services
    fun inject(service: FirebaseService)
    fun inject(service: SosNotificationSender)
}