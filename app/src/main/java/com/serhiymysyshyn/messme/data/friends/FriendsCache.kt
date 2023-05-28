package com.serhiymysyshyn.messme.data.friends

import com.serhiymysyshyn.messme.domain.friends.FriendEntity

interface FriendsCache {
    fun saveFriend(entity: FriendEntity)

    fun getFriend(key: Long): FriendEntity?

    fun getFriends(): List<FriendEntity>

    fun getFriendRequests(): List<FriendEntity>

    fun removeFriendEntity(key: Long)
}