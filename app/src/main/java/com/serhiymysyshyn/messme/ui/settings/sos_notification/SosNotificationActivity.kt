package com.serhiymysyshyn.messme.ui.settings.sos_notification

import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.serhiymysyshyn.messme.R
import com.serhiymysyshyn.messme.cache.SharedPrefsManager
import com.serhiymysyshyn.messme.domain.friends.FriendEntity
import com.serhiymysyshyn.messme.presentation.injection.CacheModule
import com.serhiymysyshyn.messme.presentation.viewmodel.RecipientViewModel
import com.serhiymysyshyn.messme.ui.App
import com.serhiymysyshyn.messme.ui.core.ext.onSuccess
import com.serhiymysyshyn.messme.ui.core.navigation.Navigator
import kotlinx.android.synthetic.main.activity_enter_pin_code.*
import kotlinx.android.synthetic.main.activity_pin_application.*
import kotlinx.android.synthetic.main.activity_sos_notification.*
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar_buttons.*
import javax.inject.Inject


class SosNotificationActivity : AppCompatActivity() {
    val cacheModule: CacheModule = CacheModule()
    private lateinit var sharedPrefsManager: SharedPrefsManager
    private lateinit var recipientsAdapter: RecipientsAdapter
    private var confirmRecipientList: MutableList<FriendEntity> = mutableListOf()
    private var sosNotificationEnabled: Boolean = false
    private var isFriendsDialogVisible: Boolean = false
    private var textProgress: Int = 0
    private var requestCounter: Int = 0

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    //@Inject
    lateinit var recipientViewModel: RecipientViewModel

    @Inject
    lateinit var navigator: Navigator

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sos_notification)

        App.appComponent.inject(this)

        recipientViewModel = viewModel {
            onSuccess(friendsToRecipientsData, ::handleFriends)
        }

//        recipientViewModel.friendsToRecipientsData.observe(this, Observer {
//            it?.let {
//                handleFriends(it)
//            }
//        })

        recipientViewModel.finishRecipientList.observe(this, Observer {
            if (it.size!=0){
                showRecipientRecyclerView()
                progressBar2.setProgress(progressBar2.progress + 34, true)
            }else{
                showEmptyRecyclerView()
                progressBar2.setProgress(progressBar2.progress - 34, true)
            }


            recipientsAdapter.submitList(it)
            sharedPrefsManager.setSosMessagePeople(convertListToString(it as MutableList<FriendEntity>))

            val listSize = it.size.toString()
            recipientCount.setText(" ($listSize)")
            confirmRecipientList = it as MutableList<FriendEntity>
        })


        init()

//        toolbar2_save_button.setOnClickListener {
//            if (sos_switch.isChecked){
//                if (!sosMessageBody.text.toString().equals("")){
//                    if (confirmRecipientList.size!=0){
//                        sharedPrefsManager.setSosMessageBody(sosMessageBody.text.toString())
//                        sharedPrefsManager.setSosMessagePeople(convertListToString(confirmRecipientList))
//                        sharedPrefsManager.setSosFunction(sos_switch.isChecked)
//
//                        Toast.makeText(this@SosNotificationActivity, "SOS-сповіщення активовані", Toast.LENGTH_LONG).show()
//                        sosNotificationEnabled = true
//                    }else{
//                        sharedPrefsManager.setSosFunction(false)
//                        Toast.makeText(this@SosNotificationActivity, "Увага! Список отримувачів порожній!", Toast.LENGTH_LONG).show()
//                    }
//                }else{
//                    sharedPrefsManager.setSosFunction(false)
//                    Toast.makeText(this@SosNotificationActivity, "Увага! Поле повідомлення екстренного сповіщення порожнє!", Toast.LENGTH_LONG).show()
//                }
//            }else{
//                sharedPrefsManager.setSosFunction(false)
//                Toast.makeText(this@SosNotificationActivity, "SOS-сповіщення деактивовані. Спочатку активуйте їх!", Toast.LENGTH_LONG).show()
//            }
//        }

        sosMessageBody.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                sharedPrefsManager.setSosMessageBody(sosMessageBody.text.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString().isNotEmpty()){
                    if (textProgress < 1){
                        progressBar2.setProgress(progressBar2.progress + 34, true)
                        textProgress = 1
                    }
                }else{
                    if (textProgress > -1){
                        progressBar2.setProgress(progressBar2.progress - 34, true)
                        textProgress = 0
                    }
                }
            }
        })

        sos_switch.setOnClickListener {
            if (sos_switch.isChecked){
                sharedPrefsManager.setSosFunction(true)
                progressBar2.setProgress(progressBar2.progress + 34, true)
            }else{
                sharedPrefsManager.setSosFunction(false)
                progressBar2.setProgress(progressBar2.progress - 34, true)
            }
        }

        del_chats_switch.setOnClickListener {
                if (del_chats_switch.isChecked){
                    SOS_indicator11.setImageResource(R.drawable.on_indicator_1)
                    sharedPrefsManager.setDeleteChatsIfSos(true)
                }else{
                    SOS_indicator11.setImageResource(R.drawable.on_indicator_0)
                    sharedPrefsManager.setDeleteChatsIfSos(false)
                }
        }

        send_gps_switch.setOnClickListener {
            if (send_gps_switch.isChecked){
                if(checkGpsPermission()){
                    SOS_indicator22.setImageResource(R.drawable.on_indicator_1)
                    sharedPrefsManager.setSendGps(true)
                }else{
                    SOS_indicator22.setImageResource(R.drawable.on_indicator_0)
                    sharedPrefsManager.setSendGps(false)
                }
            }else{
                SOS_indicator22.setImageResource(R.drawable.on_indicator_0)
                sharedPrefsManager.setSendGps(false)
            }
        }

        addRecipientSmall.setOnClickListener {
            recipientViewModel.getFriends()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun init(){
        setSupportActionBar(toolbar2)
        supportActionBar?.setTitle(R.string.sos_notification)
        toolbar2_save_button.visibility = View.GONE
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sharedPrefsManager = SharedPrefsManager(cacheModule.provideSharedPreferences(applicationContext!!))

// Stage 1------------------------------------------------------------------------------------------
        if (sharedPrefsManager.isSosActivated()){
            sos_switch.isChecked = true
            progressBar2.setProgress(progressBar2.progress + 34, true)
        }else{
            sos_switch.isChecked = false
        }

        if (sharedPrefsManager.isDeleteChatsIfSos()){
            del_chats_switch.isChecked = true
            SOS_indicator11.setImageResource(R.drawable.on_indicator_1)
        }else{
            del_chats_switch.isChecked = false
            SOS_indicator11.setImageResource(R.drawable.on_indicator_0)
        }

        if (sharedPrefsManager.isSendGps()){
            send_gps_switch.isChecked = true
            SOS_indicator22.setImageResource(R.drawable.on_indicator_1)
        }else{
            send_gps_switch.isChecked = false
            SOS_indicator22.setImageResource(R.drawable.on_indicator_0)
        }

// Stage 2------------------------------------------------------------------------------------------
        val savedSosMessage: String = sharedPrefsManager.getSosMessageBody()

        if (savedSosMessage != ""){
            sosMessageBody.setText(savedSosMessage)
            textProgress = 1
            progressBar2.setProgress(progressBar2.progress + 34, true)
        }

// Stage 3 -----------------------------------------------------------------------------------------
        if (sharedPrefsManager.getSosMessagePeople().equals("")){
            showEmptyRecyclerView()
        }else{
            progressBar2.setProgress(progressBar2.progress + 34, true)
            showRecipientRecyclerView()
        }

// Init RV -----------------------------------------------------------------------------------------
        recipientsAdapter = RecipientsAdapter()
        sos_message_people_rv.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        sos_message_people_rv.adapter = recipientsAdapter

        confirmRecipientList = readRecipientList()
        recipientCount.setText(" (${confirmRecipientList.size})")

        recipientsAdapter.submitList(confirmRecipientList)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createAddRecipientDialog(friends: List<FriendEntity>?){
        var arr_checked = BooleanArray(friends!!.size) {false}
        var arr_names = Array(friends!!.size) {""}

        for (i in 0 .. friends.size!! -1){
            arr_names[i] = friends.get(i).name
        }

        AlertDialog.Builder(this@SosNotificationActivity)
            .setTitle(R.string.choose_recipient)
            .setMultiChoiceItems(arr_names, arr_checked) { _, which, isChecked ->
                arr_checked[which] = isChecked
            }
            .setPositiveButton(R.string.save) { _, _ ->
                var list = mutableListOf<FriendEntity>()
                var _index = 0
                for (i in arr_names.indices) {
                    val checked = arr_checked[i]
                        if (checked) {
                            list.add(_index, friends.get(i))
                            _index++
                        }
                }
                isFriendsDialogVisible = false
                recipientViewModel.updateFinishRecipientList(list)
            }
            .setNegativeButton(R.string.exit) { dialog, _ ->
                isFriendsDialogVisible = false
                dialog.cancel()
            }
            .setCancelable(false)
            .create().show()
    }

    fun showRecipientRecyclerView(){
        sos_message_people_rv_layout.visibility = View.VISIBLE
        sos_message_people_empty.visibility = View.GONE
    }

    fun showEmptyRecyclerView(){
        sos_message_people_rv_layout.visibility = View.GONE
        sos_message_people_empty.visibility = View.VISIBLE
    }

    private fun handleFriends(friends: List<FriendEntity>?) {
        println(friends.toString())
        if(friends?.size == 0 || friends == null){
            while (requestCounter < 3){
                recipientViewModel.getFriends()
                requestCounter += 1
            }
            Toast.makeText(this@SosNotificationActivity, R.string.friend_list_empty, Toast.LENGTH_LONG).show()
        }else{
            requestCounter = 0
            if (!isFriendsDialogVisible){
                try{
                    isFriendsDialogVisible = true
                    createAddRecipientDialog(friends)
                }catch (e: java.lang.Exception){
                    println(">>> Error: $e")
                    Toast.makeText(this@SosNotificationActivity, "Something went wrong :( Please, try again", Toast.LENGTH_LONG).show()
                }
            }
        }
    }



    fun convertListToString(list: List<FriendEntity>): String{
        var resStr= ""
        for (frind in list){
            resStr = resStr +
                    "${frind.id}-${frind.name}-${frind.email}-${frind.friendsId}-${setNullIfClear(frind.status)}-${setNullIfClear(frind.image)}-${setNullIfClear(frind.isRequest.toString())}-${setNullIfClear(frind.lastSeen.toString())}" +
                    "#"
        }
        return resStr
    }

    private fun setNullIfClear(field: String): String{
        if (field == null || field == ""){
            return "N"
        }else{
            return field
        }
    }

    private fun setClearIfNull(field: String): String{
        if (field == "N"){
            return ""
        }else{
            return field
        }
    }

    private fun readRecipientList(): MutableList<FriendEntity>{
        val listStr = sharedPrefsManager.getSosMessagePeople()
        var recipientsList = mutableListOf<FriendEntity>()

        val stage1 = listStr.split("#")

        for (i in 0..stage1!!.size-2){
            val stage2 = stage1.get(i).split("-")

            val rId: Long = stage2.get(0).toLong()
            val rName: String = stage2.get(1)
            val rEmail: String = stage2.get(2)
            val rFriendsId: Long = stage2.get(3).toLong()
            val rStatus: String = setClearIfNull(stage2.get(4))
            val rImage: String = setClearIfNull(stage2.get(5))
            val rIsRequest: Int = setClearIfNull(stage2.get(6)).toInt()
            val rLastSeen: Long = setClearIfNull(stage2.get(7)).toLong()

            recipientsList.add(i, FriendEntity(rId, rName, rEmail, rFriendsId, rStatus, rImage, rIsRequest, rLastSeen))
        }

        return recipientsList
    }

    override fun onBackPressed() {
        if (sos_switch.isChecked){
            if (sosMessageBody.text.isNotEmpty() && confirmRecipientList.isNotEmpty()){
                Toast.makeText(this@SosNotificationActivity, "SOS-сповіщення активовані.", Toast.LENGTH_LONG).show()
                super.onBackPressed()
            }else{
                sos_switch.isChecked = false
                sharedPrefsManager.setSosFunction(false)
                super.onBackPressed()
            }
        }else{
            super.onBackPressed()
        }
    }

    inline fun <reified T : ViewModel> viewModel(body: T.() -> Unit): T {
        val vm = ViewModelProviders.of(this, viewModelFactory)[T::class.java]
        vm.body()
        return vm
    }

    private fun checkGpsPermission(): Boolean{
        if(Build.VERSION.SDK_INT >= 23){
            val accessFineLocationGranted = checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

            if (accessFineLocationGranted){

                isLocationAvailable()

//                SOS_indicator22.setImageResource(R.drawable.on_indicator_1)
//                sharedPrefsManager.setSendGps(true)
                return true
            }else{
                ActivityCompat.requestPermissions(this@SosNotificationActivity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
                return false
            }

        }else{
            isLocationAvailable()
            return true
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
                        resolvableApiException.startResolutionForResult(this@SosNotificationActivity, 222)
                    } catch (e: IntentSender.SendIntentException) { }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if (requestCode == 1){

                isLocationAvailable()

            }else{
                SOS_indicator22.setImageResource(R.drawable.on_indicator_0)
                send_gps_switch.isChecked = false
                sharedPrefsManager.setSendGps(false)
            }
        }else{
            SOS_indicator22.setImageResource(R.drawable.on_indicator_0)
            send_gps_switch.isChecked = false
            sharedPrefsManager.setSendGps(false)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 222 && resultCode == -1) {
            SOS_indicator22.setImageResource(R.drawable.on_indicator_1)
            sharedPrefsManager.setSendGps(true)
            sharedPrefsManager.setSendGps(true)
        } else {
            SOS_indicator22.setImageResource(R.drawable.on_indicator_0)
            send_gps_switch.isChecked = false
            sharedPrefsManager.setSendGps(false)
            Toast.makeText(applicationContext, "To continue you need to turn on GPS!", Toast.LENGTH_LONG).show()
        }
    }
}

