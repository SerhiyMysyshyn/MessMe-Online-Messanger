package com.serhiymysyshyn.messme.presentation.injection

import dagger.Module
import dagger.Provides
import com.serhiymysyshyn.messme.BuildConfig
import com.serhiymysyshyn.messme.data.account.AccountRemote
import com.serhiymysyshyn.messme.data.friends.FriendsRemote
import com.serhiymysyshyn.messme.data.messages.MessagesRemote
import com.serhiymysyshyn.messme.data.notifications.NotificationsRemote
import com.serhiymysyshyn.messme.data.notifications.SosNotificationsRepositoryImpl
import com.serhiymysyshyn.messme.remote.account.AccountRemoteImpl
import com.serhiymysyshyn.messme.remote.core.Request
import com.serhiymysyshyn.messme.remote.friends.FriendsRemoteImpl
import com.serhiymysyshyn.messme.remote.messages.MessagesRemoteImpl
import com.serhiymysyshyn.messme.remote.notifications.NotificationsRemoteImpl
import com.serhiymysyshyn.messme.remote.service.ApiService
import com.serhiymysyshyn.messme.remote.service.ServiceFactory
import javax.inject.Singleton

@Module
class RemoteModule {

    @Singleton
    @Provides
    fun provideApiService(): ApiService = ServiceFactory.makeService(BuildConfig.DEBUG)

    @Singleton
    @Provides
    fun provideAccountRemote(request: Request, apiService: ApiService): AccountRemote {
        return AccountRemoteImpl(request, apiService)
    }

    @Singleton
    @Provides
    fun provideFriendsRemote(request: Request, apiService: ApiService): FriendsRemote {
        return FriendsRemoteImpl(request, apiService)
    }

    @Singleton
    @Provides
    fun provideMessagesRemote(request: Request, apiService: ApiService): MessagesRemote {
        return MessagesRemoteImpl(request, apiService)
    }

    @Singleton
    @Provides
    fun provideNotificationsRemote(request: Request, apiService: ApiService): NotificationsRemote {
        return NotificationsRemoteImpl(request, apiService)
    }
}