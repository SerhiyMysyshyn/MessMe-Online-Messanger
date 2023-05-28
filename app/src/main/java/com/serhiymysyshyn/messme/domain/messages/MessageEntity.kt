package com.serhiymysyshyn.messme.domain.messages

import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity(tableName = "messages_table")
@TypeConverters(ContactConverter::class)
data class MessageEntity(
    @PrimaryKey
    @SerializedName("message_id")
    @ColumnInfo(name = "message_id")
    var id: Long,
    @SerializedName("sender_id")
    @ColumnInfo(name = "sender_id")
    var senderId: Long,
    @SerializedName("receiver_id")
    @ColumnInfo(name = "receiver_id")
    var receiverId: Long,
    var message: String,
    @SerializedName("message_date")
    @ColumnInfo(name = "message_date")
    var date: Long,
    @SerializedName("message_type_id")
    @ColumnInfo(name = "message_type_id")
    var type: Int,
    var contact: ContactEntity? = null,
    var fromMe: Boolean = false,
    @SerializedName("deleted_by_sender_id")
    @ColumnInfo(name = "deleted_by_sender_id")
    var deletedBySender: Int = 0,
    @SerializedName("deleted_by_receiver_id")
    @ColumnInfo(name = "deleted_by_receiver_id")
    var deletedByReceiver: Int = 0
) {
    constructor() : this(0L,0L,0L,"",0L,0,null, false)
}

data class ContactEntity(
    @SerializedName("user_id")
    var id: Long,
    var name: String,
    var image: String,
    @SerializedName("last_seen")
    @ColumnInfo(name = "last_seen")
    var lastSeen: Long
)

class ContactConverter {
    @TypeConverter
    fun toString(contact: ContactEntity?): String? {
        return if (contact == null) null else "${contact.id}||${contact.name}||${contact.image}||${contact.lastSeen}"
    }

    @TypeConverter
    fun toContact(string: String?): ContactEntity? {
        return if (string == null) {
            null
        } else {
            val arr = string.split("||")
            ContactEntity(arr[0].toLong(), arr[1], arr[2], arr[3].toLong())
        }
    }
}
