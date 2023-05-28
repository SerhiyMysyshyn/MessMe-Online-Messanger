package com.serhiymysyshyn.messme.remote.friends

import com.serhiymysyshyn.messme.domain.friends.FriendEntity
import com.serhiymysyshyn.messme.remote.core.BaseResponse

class GetFriendsResponse (
    success: Int,
    message: String,
    val friends: List<FriendEntity>
) : BaseResponse(success, message)