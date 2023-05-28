package com.serhiymysyshyn.messme.ui.firebase

import android.app.Service
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.location.Location
import android.os.IBinder
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.serhiymysyshyn.messme.cache.SharedPrefsManager
import com.serhiymysyshyn.messme.domain.friends.FriendEntity
import com.serhiymysyshyn.messme.domain.sosNotification.SendSosNotification
import com.serhiymysyshyn.messme.presentation.injection.CacheModule
import com.serhiymysyshyn.messme.ui.App
import javax.inject.Inject
import kotlin.concurrent.thread

class SosNotificationSender : Service() {

    @Inject
    lateinit var sendSosNotification: SendSosNotification

    val cacheModule: CacheModule = CacheModule()

    private lateinit var sharedPrefsManager: SharedPrefsManager

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var locationResult: Task<Location>? = null
    private var myLastLocation: Location? = null


    override fun onBind(intent: Intent?): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        App.appComponent.inject(this)

        sharedPrefsManager = SharedPrefsManager(cacheModule.provideSharedPreferences(context = baseContext))

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(baseContext!!)

        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var sosMessage = sharedPrefsManager.getSosMessageBody()
        val sosMessagePeopleList = readRecipientList()

        thread{
            if(sharedPrefsManager.isDeleteChatsIfSos()){
                // Delete all chats....іщі
            }
        }

        Thread {


            if (sharedPrefsManager.isSendGps()){

                loadMyLocation(sosMessagePeopleList, sosMessage)

            }else{
                for (user in sosMessagePeopleList){
                    sendSosNotification(SendSosNotification.Params(user.id, sosMessage))
                }
                stopSelf()
            }
        }.start()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun loadMyLocation(sosMessagePeopleList: List<FriendEntity>, sosMsg: String){
        initMyLocation()

        Thread.sleep(250)

        getMyLocation()

        Thread.sleep(500)

        var sosMessage = "$sosMsg \n\n(Моя геолокація: ${myLastLocation?.latitude},${myLastLocation?.longitude})"
        for (user in sosMessagePeopleList){
            sendSosNotification(SendSosNotification.Params(user.id, sosMessage))
        }
        stopSelf()
    }


    private fun initMyLocation(){
        try {
            locationResult = fusedLocationClient.lastLocation
            val executor = ContextCompat.getMainExecutor(this@SosNotificationSender)
            locationResult?.addOnCompleteListener(executor, OnCompleteListener {
                if (it.isSuccessful){
                    myLastLocation = it?.result
                }
            })
        }catch (e: SecurityException){}
    }

    private fun getMyLocation(){
        try {
            locationResult = fusedLocationClient.lastLocation
            val executor = ContextCompat.getMainExecutor(this@SosNotificationSender)
            locationResult?.addOnCompleteListener(executor, OnCompleteListener {
                if (it.isSuccessful){
                    myLastLocation = it?.result
                }
            })
        }catch (e: SecurityException){}
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

    private fun setClearIfNull(field: String): String{
        if (field == "N"){
            return ""
        }else{
            return field
        }
    }

    override fun onDestroy() {
        println(">>> Service destroyed")
        super.onDestroy()
    }
}