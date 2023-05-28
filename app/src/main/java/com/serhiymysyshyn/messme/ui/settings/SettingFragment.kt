package com.serhiymysyshyn.messme.ui.settings

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.annotation.RequiresApi
import com.serhiymysyshyn.messme.R
import com.serhiymysyshyn.messme.cache.SharedPrefsManager
import com.serhiymysyshyn.messme.presentation.injection.CacheModule
import com.serhiymysyshyn.messme.ui.App
import com.serhiymysyshyn.messme.ui.core.BaseFragment
import com.serhiymysyshyn.messme.ui.settings.chats_settings.ChatSettingsActivity
import com.serhiymysyshyn.messme.ui.settings.pin_application.PinApplicationActivity
import com.serhiymysyshyn.messme.ui.settings.sos_notification.SosNotificationActivity
import kotlinx.android.synthetic.main.fragment_setting.*


class SettingFragment : BaseFragment() {
    override val layoutId = R.layout.fragment_setting
    override val titleToolbar = R.string.settings
    val cacheModule: CacheModule = CacheModule()
    private lateinit var sharedPrefsManager: SharedPrefsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        base {
            sharedPrefsManager = SharedPrefsManager(cacheModule.provideSharedPreferences(context = context!!))

            checkActivatedFutures()

            sos_layout.setOnClickListener {
                startActivity(Intent(this, SosNotificationActivity::class.java))
            }

            pin_layout.setOnClickListener {
                startActivity(Intent(this, PinApplicationActivity::class.java))
            }

            chats_settings_layout.setOnClickListener {
                startActivity(Intent(this, ChatSettingsActivity::class.java))
            }

            accessibility_settings_layout.setOnClickListener {
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                startActivity(intent)
            }

            notification_settings_layout.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    openNotificationSettings_v2(context = baseContext)
                } else {
                    openNotificationSettings_v2(baseContext)
                }
            }


        }
    }

    private fun openNotificationSettings_v1(context: Context){
        val mManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel: NotificationChannel = mManager.getNotificationChannel(context.packageName)

            val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.getId());
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName);
            startActivity(intent)
        }
    }

    private fun openNotificationSettings_v2(context: Context){
        val settingsIntent: Intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            //.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        startActivity(settingsIntent)
    }

    fun checkActivatedFutures(){
        isSosNotificationActivated()
        isPinAppActivated()
    }

    fun isSosNotificationActivated(){
        if (sharedPrefsManager.isSosActivated()){
            onIndicator_SOS.setImageResource(R.drawable.on_indicator_1)
        }else{
            onIndicator_SOS.setImageResource(R.drawable.on_indicator_0)
        }
    }

    fun isPinAppActivated(){
        if (sharedPrefsManager.isPinAppActivated()){
            onIndicator_PIN.setImageResource(R.drawable.on_indicator_1)
        }else{
            onIndicator_PIN.setImageResource(R.drawable.on_indicator_0)
        }
    }

    override fun onResume() {
        checkActivatedFutures()
        super.onResume()
    }
}