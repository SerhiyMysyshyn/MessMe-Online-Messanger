package com.serhiymysyshyn.messme.ui.login

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.core.content.ContextCompat
import com.serhiymysyshyn.messme.R
import com.serhiymysyshyn.messme.cache.SharedPrefsManager
import com.serhiymysyshyn.messme.presentation.injection.CacheModule
import com.serhiymysyshyn.messme.ui.App
import com.serhiymysyshyn.messme.ui.firebase.SosNotificationSender
import com.serhiymysyshyn.messme.ui.home.HomeActivity
import kotlinx.android.synthetic.main.activity_enter_pin_code.*
import java.time.LocalTime
import kotlin.concurrent.thread

class EnterPinCodeActivity : AppCompatActivity() {
    val cacheModule: CacheModule = CacheModule()
    private lateinit var sharedPrefsManager: SharedPrefsManager
    private lateinit var vibrator: Vibrator
    private var canVibrate: Boolean = false
    val milliseconds = 10L

    private var userPin = ""

    // Use biometric
    lateinit var biometricPrompt: BiometricPrompt
    lateinit var promptInfo: PromptInfo


    private val startPosition: Int = 1
    private val endPosition: Int = 5

    private var repeatCount = 1
    private var firstTouch: Long = 0
    private var lastTouch: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_pin_code)

        App.appComponent.inject(this)

        vibrator = this@EnterPinCodeActivity.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        canVibrate = vibrator.hasVibrator()
        sharedPrefsManager = SharedPrefsManager(cacheModule.provideSharedPreferences(applicationContext!!))

        initKeyboard()

        if (sharedPrefsManager.isBiometricActivated()){
            startBiometricCheck()
        }

        userPin = sharedPrefsManager.getPinAppCode()

        e_editTextNumberPassword.addTextChangedListener(object : TextWatcher {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().equals(userPin)){
                    disableKeyboard()

                    thread {
                        progressBar3.setProgress(100, true)
                    }

                    thread {
                        Thread.sleep(20)
                        startActivity(Intent(this@EnterPinCodeActivity, HomeActivity::class.java))
                        finish()
                    }

                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

    private fun startBiometricCheck(){
        fingerprint_icon.visibility = View.VISIBLE
        val biometricManager = BiometricManager.from(baseContext)
        when(biometricManager.canAuthenticate()){
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> Toast.makeText(baseContext, "Device doesn't have fingerprint", Toast.LENGTH_SHORT).show()
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> Toast.makeText(baseContext, "Fingerprint doesn't work", Toast.LENGTH_SHORT).show()
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> Toast.makeText(baseContext, "No fingerprint assigned", Toast.LENGTH_SHORT).show()
        }
        val executor = ContextCompat.getMainExecutor(this@EnterPinCodeActivity)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                }
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    disableKeyboard()
                    startActivity(Intent(this@EnterPinCodeActivity, HomeActivity::class.java))
                    finish()
                }
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(resources.getString(R.string.biometric_view_title))
            .setSubtitle(resources.getString(R.string.biometric_view_main_text))
            .setNegativeButtonText(resources.getString(R.string.biometric_view_use_pin_code))
            .build()
            biometricPrompt.authenticate(promptInfo)
    }

    fun setClickVibrate(duration: Long = milliseconds){
        if (canVibrate) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(duration)
            }
        }
    }

    private fun initKeyboard(){
        e_button_0.setOnClickListener {
            setClickVibrate()
            e_editTextNumberPassword.setText(e_editTextNumberPassword.text.toString() + "0")
        }
        e_button_1.setOnClickListener {
            setClickVibrate()
            e_editTextNumberPassword.setText(e_editTextNumberPassword.text.toString() + "1")
        }
        e_button_2.setOnClickListener {
            setClickVibrate()
            e_editTextNumberPassword.setText(e_editTextNumberPassword.text.toString() + "2")
        }
        e_button_3.setOnClickListener {
            setClickVibrate()
            e_editTextNumberPassword.setText(e_editTextNumberPassword.text.toString() + "3")
        }
        e_button_4.setOnClickListener {
            setClickVibrate()
            e_editTextNumberPassword.setText(e_editTextNumberPassword.text.toString() + "4")
        }
        e_button_5.setOnClickListener {
            setClickVibrate()
            e_editTextNumberPassword.setText(e_editTextNumberPassword.text.toString() + "5")
        }
        e_button_6.setOnClickListener {
            setClickVibrate()
            e_editTextNumberPassword.setText(e_editTextNumberPassword.text.toString() + "6")
        }
        e_button_7.setOnClickListener {
            setClickVibrate()
            e_editTextNumberPassword.setText(e_editTextNumberPassword.text.toString() + "7")
        }
        e_button_8.setOnClickListener {
            setClickVibrate()
            e_editTextNumberPassword.setText(e_editTextNumberPassword.text.toString() + "8")
        }
        e_button_9.setOnClickListener {
            setClickVibrate()
            e_editTextNumberPassword.setText(e_editTextNumberPassword.text.toString() + "9")
        }

        e_button_clear.setOnClickListener {
            setClickVibrate()
            if (e_editTextNumberPassword.text.isNotEmpty()) {
                e_editTextNumberPassword.setText(e_editTextNumberPassword.text.toString().substring(0, e_editTextNumberPassword.text.toString().length - 1))
            }
        }

        e_button_clear.setOnLongClickListener {
            if (e_editTextNumberPassword.text.isNotEmpty()) {
                setClickVibrate(100)
                e_editTextNumberPassword.setText("")
            }
            true
        }
    }

    private fun disableKeyboard(){
        e_button_0.isEnabled = false
        e_button_1.isEnabled = false
        e_button_2.isEnabled = false
        e_button_3.isEnabled = false
        e_button_4.isEnabled = false
        e_button_5.isEnabled = false
        e_button_6.isEnabled = false
        e_button_7.isEnabled = false
        e_button_8.isEnabled = false
        e_button_9.isEnabled = false
        e_button_clear.isEnabled = false
        e_button_fingerprint.isEnabled = false
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
                Toast.makeText(baseContext, "Sending SOS-Notification...", Toast.LENGTH_SHORT).show()
            }
        }
    }
}