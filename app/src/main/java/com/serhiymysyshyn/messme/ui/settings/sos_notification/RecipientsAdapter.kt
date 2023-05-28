package com.serhiymysyshyn.messme.ui.settings.sos_notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.serhiymysyshyn.messme.databinding.ItemNewConversationBinding
import com.serhiymysyshyn.messme.domain.friends.FriendEntity
import com.serhiymysyshyn.messme.ui.core.BaseAdapter

open class RecipientsAdapter : BaseAdapter<FriendEntity, RecipientsAdapter.RecipientsViewHolder>(RecipientsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipientsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemNewConversationBinding.inflate(layoutInflater, parent, false)
        return RecipientsViewHolder(binding = binding)
    }

    class RecipientsViewHolder(val binding: ItemNewConversationBinding) : BaseViewHolder(binding.root) {

        init {
            binding.rlMain.setOnClickListener {
                onClick?.onClick(item, it)
            }
        }

        override fun onBind(item: Any) {
            (item as? FriendEntity)?.let {
                binding.friend = it
            }
        }
    }



    class RecipientsDiffCallback : DiffUtil.ItemCallback<FriendEntity>() {

        override fun areItemsTheSame(oldItem: FriendEntity, newItem: FriendEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FriendEntity, newItem: FriendEntity): Boolean {
            return oldItem == newItem
        }
    }
}