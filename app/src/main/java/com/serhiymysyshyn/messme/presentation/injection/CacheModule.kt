package com.serhiymysyshyn.messme.presentation.injection

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import com.serhiymysyshyn.messme.cache.AccountCacheImpl
import com.serhiymysyshyn.messme.cache.ChatDatabase
import com.serhiymysyshyn.messme.cache.SharedPrefsManager
import com.serhiymysyshyn.messme.data.account.AccountCache
import com.serhiymysyshyn.messme.data.friends.FriendsCache
import com.serhiymysyshyn.messme.data.messages.MessagesCache
import javax.inject.Singleton

@Module
class CacheModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideAccountCache(prefsManager: SharedPrefsManager, chatDatabase: ChatDatabase): AccountCache = AccountCacheImpl(prefsManager, chatDatabase)

    @Provides
    @Singleton
    fun provideChatDatabase(context: Context): ChatDatabase {
        return ChatDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideFriendsCache(chatDatabase: ChatDatabase): FriendsCache {
        return chatDatabase.friendsDao
    }

    @Provides
    @Singleton
    fun provideMessagesCache(chatDatabase: ChatDatabase): MessagesCache {
        return chatDatabase.messagesDao
    }
}