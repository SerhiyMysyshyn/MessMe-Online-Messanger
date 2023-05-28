package com.serhiymysyshyn.messme.ui.core

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.serhiymysyshyn.messme.R
import com.serhiymysyshyn.messme.cache.SharedPrefsManager
import com.serhiymysyshyn.messme.domain.type.Failure
import com.serhiymysyshyn.messme.presentation.injection.CacheModule
import com.serhiymysyshyn.messme.presentation.viewmodel.RecipientViewModel
import com.serhiymysyshyn.messme.ui.core.navigation.Navigator
import com.serhiymysyshyn.messme.ui.firebase.SosNotificationSender
import kotlinx.android.synthetic.main.activity_sos_notification.*
import kotlinx.android.synthetic.main.toolbar.*
import java.time.LocalTime
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {

    abstract var fragment: BaseFragment

    val cacheModule: CacheModule = CacheModule()
    private lateinit var sharedPrefsManager: SharedPrefsManager

    private val startPosition: Int = 1
    private val endPosition: Int = 5

    private var repeatCount = 1
    private var firstTouch: Long = 0
    private var lastTouch: Long = 0


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var permissionManager: PermissionManager

    open val contentId = R.layout.activity_layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupContent()

        setSupportActionBar(toolbar)
        addFragment(savedInstanceState)

        sharedPrefsManager = SharedPrefsManager(cacheModule.provideSharedPreferences(context = baseContext))

        if(sharedPrefsManager.isSendGps()){
            isLocationAvailable()
        }
    }

    open fun setupContent() {
        setContentView(contentId)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        fragment.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 333 && resultCode == -1) {

        } else {
            sharedPrefsManager.setSendGps(false)
            Toast.makeText(applicationContext, "GPS не будуть надіслані під час виклику SOS-функції! Активуйте їх в налаштуваннях!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onBackPressed() {
        (supportFragmentManager.findFragmentById(
            R.id.fragmentContainer
        ) as BaseFragment).onBackPressed()
        super.onBackPressed()
    }

    fun addFragment(savedInstanceState: Bundle? = null, fragment: BaseFragment = this.fragment) {
        savedInstanceState ?: supportFragmentManager.inTransaction {
            add(R.id.fragmentContainer, fragment)
        }
    }

    fun replaceFragment(fragment: BaseFragment) {
        this.fragment = fragment
        supportFragmentManager.inTransaction {
            replace(R.id.fragmentContainer, fragment)
        }
    }


    fun showProgress() = progressStatus(View.VISIBLE)

    fun hideProgress() = progressStatus(View.GONE)

    fun progressStatus(viewStatus: Int) {
        toolbar_progress_bar.visibility = viewStatus
    }


    fun hideSoftKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    open fun handleFailure(failure: Failure?) {
        hideProgress()
        when (failure) {
            is Failure.NetworkConnectionError -> showMessage(getString(R.string.error_network))
            is Failure.ServerError -> showMessage(getString(R.string.error_server))
            is Failure.EmailAlreadyExistError -> showMessage(getString(R.string.error_email_already_exist))
            is Failure.AuthError -> showMessage(getString(R.string.error_auth))
            is Failure.TokenError -> navigator.showLogin(this)
            is Failure.AlreadyFriendError -> showMessage(getString(R.string.error_already_friend))
            is Failure.AlreadyRequestedFriendError -> showMessage(getString(R.string.error_already_requested_friend))
            is Failure.FilePickError -> showMessage(getString(R.string.error_picking_file))
            is Failure.EmailNotRegisteredError -> showMessage(getString(R.string.email_not_registered))
            is Failure.CantSendEmailError -> showMessage(getString(R.string.error_cant_send_email))
        }
    }

    fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    inline fun <reified T : ViewModel> viewModel(body: T.() -> Unit): T {
        val vm = ViewModelProviders.of(this, viewModelFactory)[T::class.java]
        vm.body()
        return vm
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionManager.requestObject?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_DOWN ->  {

                if (repeatCount == startPosition){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        //firstTouch = LocalTime.now().hour + LocalTime.now().minute + LocalTime.now().second + LocalTime.now().nano
                        firstTouch = LocalTime.now().toNanoOfDay()
                        //println("First touch (${LocalTime.now()}): $firstTouch")
                    }
                }

                if (repeatCount == endPosition){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        //lastTouch = LocalTime.now().hour + LocalTime.now().minute + LocalTime.now().second + LocalTime.now().nano
                        lastTouch = LocalTime.now().toNanoOfDay()
                        //println("Last touch (${LocalTime.now()}): $lastTouch")
                    }

                    var delay = lastTouch-firstTouch
                    println("Delay: $delay")

                    if (delay < 1200000000L){
                        canSendSosNotification(repeatCount)
                    }
                }

                if (repeatCount == endPosition){ repeatCount = 1
                }else{ repeatCount = repeatCount + 1 }

                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun canSendSosNotification(repeatCount: Int){
        if(sharedPrefsManager.isSosActivated()){
            if (repeatCount == endPosition){
                startService(Intent(this, SosNotificationSender::class.java))
                showMessage("Sending SOS-Notification...")
            }
        }
    }

    private fun isLocationAvailable() {
        val mLocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval((10 * 1000).toLong())
            .setFastestInterval((1 * 1000).toLong())
        val settingsBuilder = LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest)
        settingsBuilder.setAlwaysShow(true)
        val result = LocationServices.getSettingsClient(this).checkLocationSettings(settingsBuilder.build())
        result.addOnCompleteListener { task ->
            try {
                val response = task.getResult(
                    ApiException::class.java
                )

            } catch (ex: ApiException) {
                when (ex.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val resolvableApiException = ex as ResolvableApiException
                        resolvableApiException.startResolutionForResult(this@BaseActivity, 333)
                    } catch (e: IntentSender.SendIntentException) { }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                }
            }
        }
    }

}


inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) =
    beginTransaction().func().commit()

inline fun Activity?.base(block: BaseActivity.() -> Unit) {
    (this as? BaseActivity)?.let(block)
}

