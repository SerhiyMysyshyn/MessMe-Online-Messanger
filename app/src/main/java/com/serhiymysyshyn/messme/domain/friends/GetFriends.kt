package com.serhiymysyshyn.messme.domain.friends

import com.serhiymysyshyn.messme.domain.interactor.UseCase
import javax.inject.Inject

class GetFriends @Inject constructor(
    private val friendsRepository: FriendsRepository
) : UseCase<List<FriendEntity>, Boolean>() {

    override suspend fun run(needFetch: Boolean) = friendsRepository.getFriends(needFetch)
}