package com.serhiymysyshyn.messme.cache

import android.content.SharedPreferences
import com.serhiymysyshyn.messme.domain.account.AccountEntity
import com.serhiymysyshyn.messme.domain.type.Either
import com.serhiymysyshyn.messme.domain.type.None
import com.serhiymysyshyn.messme.domain.type.Failure
import javax.inject.Inject

class SharedPrefsManager @Inject constructor(private val prefs: SharedPreferences) {
    companion object {
        const val ACCOUNT_TOKEN = "account_token"
        const val ACCOUNT_ID = "account_id"
        const val ACCOUNT_NAME = "account_name"
        const val ACCOUNT_EMAIL = "account_email"
        const val ACCOUNT_STATUS = "account_status"
        const val ACCOUNT_DATE = "account_date"
        const val ACCOUNT_IMAGE = "account_image"
        const val ACCOUNT_PASSWORD = "account_password"

        const val SOS_ACTIVE = "sos_active"
        const val SOS_MESSAGE_DELETE_CHATS = "sos_message_delete_chats"
        const val SOS_MESSAGE_SEND_GPS = "sos_message_send_gps"
        const val SOS_MESSAGE_BODY = "sos_message_body"
        const val SOS_MESSAGE_PEOPLE = "sos_message_people"

        const val PIN_APP_ACTIVE = "pin_app_active"
        const val PIN_APP_CODE = "pin_app_code"
        const val USE_BIOMETRIC = "use_biometric"
    }

// Security ----------------------------------------------------------------------------------------

    fun setSosFunction(isActive: Boolean): Either<Failure, None>{
        prefs.edit().apply {
            putBoolean(SOS_ACTIVE, isActive)
        }.apply()

        return Either.Right(None())
    }

    fun isSosActivated(): Boolean{
        return prefs.getBoolean(SOS_ACTIVE, false)
    }

    fun setSosMessageBody(msg: String): Either<Failure, None>{
        prefs.edit().apply {
            putString(SOS_MESSAGE_BODY, msg)
        }.apply()
        return Either.Right(None())
    }


    fun setDeleteChatsIfSos(isActive: Boolean): Either<Failure, None>{
        prefs.edit().apply {
            putBoolean(SOS_MESSAGE_DELETE_CHATS, isActive)
        }.apply()

        return Either.Right(None())
    }

    fun isDeleteChatsIfSos(): Boolean{
        return prefs.getBoolean(SOS_MESSAGE_DELETE_CHATS, false)
    }

    fun setSendGps(isActive: Boolean): Either<Failure, None>{
        prefs.edit().apply {
            putBoolean(SOS_MESSAGE_SEND_GPS, isActive)
        }.apply()

        return Either.Right(None())
    }

    fun isSendGps(): Boolean{
        return prefs.getBoolean(SOS_MESSAGE_SEND_GPS, false)
    }


    fun getSosMessageBody(): String{
        return prefs.getString(SOS_MESSAGE_BODY, "")
    }

    fun setSosMessagePeople(peopleArray: String): Either<Failure, None>{
        prefs.edit().apply {
            putString(SOS_MESSAGE_PEOPLE, peopleArray)
        }.apply()
        return Either.Right(None())
    }

    fun getSosMessagePeople(): String{
        return prefs.getString(SOS_MESSAGE_PEOPLE, "")
    }

// -----------------------------------------------------------------------
    fun setPinFunction(isActive: Boolean): Either<Failure, None>{
        prefs.edit().apply {
            putBoolean(PIN_APP_ACTIVE, isActive)
        }.apply()

        return Either.Right(None())
    }

    fun isPinAppActivated(): Boolean{
        return prefs.getBoolean(PIN_APP_ACTIVE, false)
    }

    fun setBiometricFunction(isActive: Boolean): Either<Failure, None>{
        prefs.edit().apply {
            putBoolean(USE_BIOMETRIC, isActive)
        }.apply()

        return Either.Right(None())
    }

    fun isBiometricActivated(): Boolean{
        return prefs.getBoolean(USE_BIOMETRIC, false)
    }

    fun setPinCode(code: String): Either<Failure, None>{
        prefs.edit().apply {
            putString(PIN_APP_CODE, code)
        }.apply()

        return Either.Right(None())
    }

    fun getPinAppCode(): String{
        return prefs.getString(PIN_APP_CODE, "")
    }


// -------------------------------------------------------------------------------------------------



    fun saveToken(token: String): Either<Failure, None> {
        prefs.edit().apply {
            putString(ACCOUNT_TOKEN, token)
        }.apply()

        return Either.Right(None())
    }

    fun getToken(): Either<Failure, String> {
        return Either.Right(prefs.getString(ACCOUNT_TOKEN, ""))
    }

    fun saveAccount(account: AccountEntity): Either<Failure, None> {
        prefs.edit().apply {
            putSafely(ACCOUNT_ID, account.id)
            putSafely(ACCOUNT_NAME, account.name)
            putSafely(ACCOUNT_EMAIL, account.email)
            putSafely(ACCOUNT_TOKEN, account.token)
            putString(ACCOUNT_STATUS, account.status)
            putSafely(ACCOUNT_DATE, account.userDate)
            putSafely(ACCOUNT_IMAGE, account.image)
            putSafely(ACCOUNT_PASSWORD, account.password)
        }.apply()

        return Either.Right(None())
    }

    fun getAccount(): Either<Failure, AccountEntity> {
        val id = prefs.getLong(ACCOUNT_ID, 0)

        if (id == 0L) {
            return Either.Left(Failure.NoSavedAccountsError)
        }

        val account = AccountEntity(
            prefs.getLong(ACCOUNT_ID, 0),
            prefs.getString(ACCOUNT_NAME, "")!!,
            prefs.getString(ACCOUNT_EMAIL, "")!!,
            prefs.getString(ACCOUNT_TOKEN, "")!!,
            prefs.getString(ACCOUNT_STATUS, "")!!,
            prefs.getLong(ACCOUNT_DATE, 0),
            prefs.getString(ACCOUNT_IMAGE, "")!!,
            prefs.getString(ACCOUNT_PASSWORD, "")!!
        )

        return Either.Right(account)
    }

    fun removeAccount(): Either<Failure, None> {
        prefs.edit().apply {
            remove(ACCOUNT_ID)
            remove(ACCOUNT_NAME)
            remove(ACCOUNT_EMAIL)
            remove(ACCOUNT_STATUS)
            remove(ACCOUNT_DATE)
            remove(ACCOUNT_IMAGE)
            remove(ACCOUNT_PASSWORD)

            remove(SOS_ACTIVE)
            remove(SOS_MESSAGE_BODY)
            remove(SOS_MESSAGE_PEOPLE)

            remove(PIN_APP_ACTIVE)
            remove(PIN_APP_CODE)
        }.apply()

        return Either.Right(None())
    }

    fun containsAnyAccount(): Either<Failure, Boolean> {
        val id = prefs.getLong(ACCOUNT_ID, 0)
        return Either.Right(id != 0L)
    }

}

fun SharedPreferences.Editor.putSafely(key: String, value: Long?) {
    if (value != null && value != 0L) {
        putLong(key, value)
    }
}

fun SharedPreferences.Editor.putSafely(key: String, value: String?) {
    if (value != null && value.isNotEmpty()) {
        putString(key, value)
    }
}