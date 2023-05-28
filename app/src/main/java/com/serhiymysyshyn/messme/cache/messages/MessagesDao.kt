package com.serhiymysyshyn.messme.cache.messages

import androidx.lifecycle.LiveData
import androidx.room.*
import com.serhiymysyshyn.messme.data.messages.MessagesCache
import com.serhiymysyshyn.messme.domain.messages.MessageEntity

@Dao
interface MessagesDao : MessagesCache {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: MessageEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entities: List<MessageEntity>): List<Long>

    @Update
    fun update(entity: MessageEntity)

    @Transaction
    override fun saveMessage(entity: MessageEntity) {
        if (insert(entity) == -1L) {
            update(entity)
        }
    }

    @Transaction
    override fun saveMessages(entities: List<MessageEntity>) {
        insert(entities)
    }

    @Query("SELECT * from messages_table ORDER BY message_date DESC")
    override fun getChats(): List<MessageEntity>

    @Query("SELECT * from messages_table ORDER BY message_date DESC")
    fun getLiveChats(): LiveData<List<MessageEntity>>

    @Query("SELECT * from messages_table WHERE (deleted_by_receiver_id = 0 AND deleted_by_sender_id = 0) AND (sender_id = :contactId OR receiver_id = :contactId) ORDER BY message_date ASC")
    override fun getMessagesWithContact(contactId: Long): List<MessageEntity>

    @Query("SELECT * from messages_table WHERE (deleted_by_receiver_id = 0 AND deleted_by_sender_id = 0) AND (sender_id = :contactId OR receiver_id = :contactId) ORDER BY message_date ASC")
    fun getLiveMessagesWithContact(contactId: Long): LiveData<List<MessageEntity>>

    @Query("DELETE from messages_table WHERE message_id = :messageId")
    override fun deleteMessagesByUser(messageId: Long)

    @Query("DELETE from messages_table WHERE (sender_id = :senderId AND receiver_id = :receiverId) OR (sender_id = :receiverId AND receiver_id = :senderId)")
    override fun deleteChat(senderId: Int, receiverId: Int)
}