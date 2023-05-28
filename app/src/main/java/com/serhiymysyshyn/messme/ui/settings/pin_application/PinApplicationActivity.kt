package com.serhiymysyshyn.messme.ui.settings.pin_application

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.serhiymysyshyn.messme.R
import com.serhiymysyshyn.messme.cache.SharedPrefsManager
import com.serhiymysyshyn.messme.presentation.injection.CacheModule
import com.serhiymysyshyn.messme.ui.App
import kotlinx.android.synthetic.main.activity_enter_pin_code.*
import kotlinx.android.synthetic.main.activity_pin_application.*
import kotlinx.android.synthetic.main.activity_pin_application.textView6
import kotlinx.android.synthetic.main.activity_sos_notification.*
import kotlinx.android.synthetic.main.toolbar_buttons.*

class PinApplicationActivity : AppCompatActivity() {
    val cacheModule: CacheModule = CacheModule()
    private lateinit var sharedPrefsManager: SharedPrefsManager
    private var passwordIsEmpty = true
    private var passwordInStage1 = ""

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin_application)

        App.appComponent.inject(this)

        init()

        pin_app_switch.setOnClickListener {
            if (pin_app_switch.isChecked){
                sharedPrefsManager.setPinFunction(true)
                progressBar.setProgress(progressBar.progress + 50, true)
            }else{
                sharedPrefsManager.setPinFunction(false)
                progressBar.setProgress(progressBar.progress - 50, true)
            }
        }

        use_fingerprint_switch.setOnClickListener {
            if (use_fingerprint_switch.isChecked){
                SOS_indicator33.setImageResource(R.drawable.on_indicator_1)
                sharedPrefsManager.setBiometricFunction(true)
            }else{
                SOS_indicator33.setImageResource(R.drawable.on_indicator_0)
                sharedPrefsManager.setBiometricFunction(false)
            }
        }

    }

    private fun savePinCode_stage1(pinCode: String){
        println("STAGE 1")
        if (pinCode.length >=4){
            passwordInStage1 = pinCode
            editTextNumberPassword.setText("")
            button_ok.setText("NEXT")
            textView6.setText(R.string.reenter_new_pin)
        }else{
            Toast.makeText(this@PinApplicationActivity, "PIN-код повинен містити мінімум 4 цифри", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun savePinCode_stage2(pinCode: String){
        println("STAGE 2")
        if (pinCode.equals(passwordInStage1)){
            if (pin_app_switch.isChecked){
                sharedPrefsManager.setPinCode(pinCode)
                textView6.setText(R.string.enter_new_pin)
                progressBar.setProgress(progressBar.progress + 50, true)
                Toast.makeText(this@PinApplicationActivity, "PIN-код успішно встановлено", Toast.LENGTH_SHORT).show()
                passwordInStage1 = ""
                button_ok.setText("OK")

            }else{
                Toast.makeText(this@PinApplicationActivity, "Функція PIN-коду деактивована. Спочатку активуйте її!", Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(this@PinApplicationActivity, "PIN-коди не співпадають", Toast.LENGTH_SHORT).show()
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun init(){
        setSupportActionBar(toolbar2)
        supportActionBar?.setTitle(R.string.pin_my_app)
        toolbar2_save_button.visibility = View.GONE
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sharedPrefsManager = SharedPrefsManager(cacheModule.provideSharedPreferences(applicationContext!!))

        if (sharedPrefsManager.isPinAppActivated()){
            pin_app_switch.isChecked = true
        }else{
            pin_app_switch.isChecked = false
        }

        if (sharedPrefsManager.isBiometricActivated()){
            SOS_indicator33.setImageResource(R.drawable.on_indicator_1)
            use_fingerprint_switch.isChecked = true
        }else{
            SOS_indicator33.setImageResource(R.drawable.on_indicator_0)
            use_fingerprint_switch.isChecked = false
        }

        val pin = sharedPrefsManager.getPinAppCode()

        editTextNumberPassword.setText(pin)

        if (pin.isEmpty()){
            button_ok.setText("NEXT")
        }else{
            button_ok.text = "OK"
        }

        if (pin_app_switch.isChecked){
            progressBar.setProgress(progressBar.progress + 50, true)
        }

        if (editTextNumberPassword.text.toString().isNotEmpty()){
            passwordIsEmpty = false
            progressBar.setProgress(progressBar.progress + 50, true)
        }


        button_0.setOnClickListener {
            editTextNumberPassword.setText(editTextNumberPassword.text.toString() + "0")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) VibrationEffect.createOneShot(1000L, -1)
        }
        button_1.setOnClickListener {
            editTextNumberPassword.setText(editTextNumberPassword.text.toString() + "1")
        }
        button_2.setOnClickListener {
            editTextNumberPassword.setText(editTextNumberPassword.text.toString() + "2")
        }
        button_3.setOnClickListener {
            editTextNumberPassword.setText(editTextNumberPassword.text.toString() + "3")
        }
        button_4.setOnClickListener {
            editTextNumberPassword.setText(editTextNumberPassword.text.toString() + "4")
        }
        button_5.setOnClickListener {
            editTextNumberPassword.setText(editTextNumberPassword.text.toString() + "5")
        }
        button_6.setOnClickListener {
            editTextNumberPassword.setText(editTextNumberPassword.text.toString() + "6")
        }
        button_7.setOnClickListener {
            editTextNumberPassword.setText(editTextNumberPassword.text.toString() + "7")
        }
        button_8.setOnClickListener {
            editTextNumberPassword.setText(editTextNumberPassword.text.toString() + "8")
        }
        button_9.setOnClickListener {
            editTextNumberPassword.setText(editTextNumberPassword.text.toString() + "9")
        }
        button_ok.setOnClickListener {
            if (pin_app_switch.isChecked){
                if (sharedPrefsManager.getPinAppCode().equals(editTextNumberPassword.text.toString())){
                    Toast.makeText(this@PinApplicationActivity, "Даний PIN-код вже використовується! Будь ласка, введіть новий PIN-код.", Toast.LENGTH_SHORT).show()
                }else{
                    if (passwordInStage1.isEmpty()){
                        savePinCode_stage1(editTextNumberPassword.text.toString())
                    }else if (passwordInStage1.isNotEmpty()){
                        savePinCode_stage2(editTextNumberPassword.text.toString())
                    }
                }
            }else{
                Toast.makeText(this@PinApplicationActivity, "Функція PIN-коду деактивована. Спочатку активуйте її!", Toast.LENGTH_LONG).show()
            }
        }
        button_clear.setOnClickListener {
            if (editTextNumberPassword.text.isNotEmpty()) {
                editTextNumberPassword.setText(editTextNumberPassword.text.toString().substring(0, editTextNumberPassword.text.toString().length - 1))
            }
        }

        button_clear.setOnLongClickListener {
            if (editTextNumberPassword.text.isNotEmpty()) {
                editTextNumberPassword.setText("")
            }
            true
        }

        editTextNumberPassword.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        editTextNumberPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
                    }
                    MotionEvent.ACTION_UP ->{
                        editTextNumberPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    }
                }

                return v?.onTouchEvent(event) ?: true
            }
        })

        editTextNumberPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isEmpty()){
                    if (pin_app_switch.isChecked && progressBar.progress == 50){
                    }else{
                        progressBar.setProgress(progressBar.progress - 50, true)
                    }
                    println(">>> -")
                    passwordIsEmpty = true
                }else{
                    if (passwordIsEmpty){
                        println(">>> +")
                        passwordIsEmpty = false
                    }
                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBackPressed() {
        if (pin_app_switch.isChecked){
            if (editTextNumberPassword.text.toString().isNotEmpty()){
                Toast.makeText(this@PinApplicationActivity, "Функція PIN-код успішно встановлена", Toast.LENGTH_SHORT).show()
                super.onBackPressed()
            }else{
                progressBar.setProgress(progressBar.progress - 50, true)
                pin_app_switch.isChecked = false
                sharedPrefsManager.setPinFunction(false)
                super.onBackPressed()
            }
        }else{
            super.onBackPressed()
        }


    }
}