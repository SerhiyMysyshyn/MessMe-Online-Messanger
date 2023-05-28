package com.serhiymysyshyn.messme.domain.friends

import com.serhiymysyshyn.messme.domain.interactor.UseCase
import com.serhiymysyshyn.messme.domain.type.None
import javax.inject.Inject

class CancelFriendRequest @Inject constructor(
    private val friendsRepository: FriendsRepository
) : UseCase<None, FriendEntity>() {

    override suspend fun run(params: FriendEntity) = friendsRepository.cancelFriendRequest(params)
}