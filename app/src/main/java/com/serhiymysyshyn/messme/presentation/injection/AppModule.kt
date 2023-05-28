package com.serhiymysyshyn.messme.presentation.injection

import android.content.Context
import dagger.Module
import dagger.Provides
import com.serhiymysyshyn.messme.data.account.AccountCache
import com.serhiymysyshyn.messme.data.account.AccountRemote
import com.serhiymysyshyn.messme.data.account.AccountRepositoryImpl
import com.serhiymysyshyn.messme.data.friends.FriendsCache
import com.serhiymysyshyn.messme.data.friends.FriendsRemote
import com.serhiymysyshyn.messme.data.friends.FriendsRepositoryImpl
import com.serhiymysyshyn.messme.data.media.MediaRepositoryImpl
import com.serhiymysyshyn.messme.data.messages.MessagesCache
import com.serhiymysyshyn.messme.data.messages.MessagesRemote
import com.serhiymysyshyn.messme.data.messages.MessagesRepositoryImpl
import com.serhiymysyshyn.messme.data.notifications.NotificationsRemote
import com.serhiymysyshyn.messme.data.notifications.SosNotificationsRepositoryImpl
import com.serhiymysyshyn.messme.domain.account.AccountRepository
import com.serhiymysyshyn.messme.domain.friends.FriendsRepository
import com.serhiymysyshyn.messme.domain.media.MediaRepository
import com.serhiymysyshyn.messme.domain.messages.MessagesRepository
import com.serhiymysyshyn.messme.domain.sosNotification.SosNotificationRepository
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideAppContext(): Context = context

    @Provides
    @Singleton
    fun provideAccountRepository(remote: AccountRemote, cache: AccountCache): AccountRepository {
        return AccountRepositoryImpl(remote, cache)
    }

    @Provides
    @Singleton
    fun provideFriendsRepository(remote: FriendsRemote, accountCache: AccountCache, friendsCache: FriendsCache): FriendsRepository {
        return FriendsRepositoryImpl(accountCache, remote, friendsCache)
    }

    @Provides
    @Singleton
    fun provideMediaRepository(context: Context): MediaRepository {
        return MediaRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideMessagesRepository(remote: MessagesRemote, cache: MessagesCache, accountCache: AccountCache): MessagesRepository {
        return MessagesRepositoryImpl(remote, cache, accountCache)
    }

    @Provides
    @Singleton
    fun provideSosNotificationRepository(remote: NotificationsRemote, accountCache: AccountCache): SosNotificationRepository {
        return SosNotificationsRepositoryImpl(remote, accountCache)
    }
}