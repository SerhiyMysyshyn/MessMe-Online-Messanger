package com.serhiymysyshyn.messme.remote.friends

import com.google.gson.annotations.SerializedName
import com.serhiymysyshyn.messme.domain.friends.FriendEntity
import com.serhiymysyshyn.messme.remote.core.BaseResponse

class GetFriendRequestsResponse (
    success: Int,
    message: String,
    @SerializedName("friend_requests")
    val friendsRequests: List<FriendEntity>
) : BaseResponse(success, message)