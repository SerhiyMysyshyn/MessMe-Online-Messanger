package com.serhiymysyshyn.messme.ui.friends

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.serhiymysyshyn.messme.databinding.ItemNewConversationBinding
import com.serhiymysyshyn.messme.domain.friends.FriendEntity
import com.serhiymysyshyn.messme.ui.core.BaseAdapter

open class SimpleFriendsAdapter : BaseAdapter<FriendEntity, SimpleFriendsAdapter.SimpleFriendViewHolder>(FriendsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleFriendViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemNewConversationBinding.inflate(layoutInflater, parent, false)
        return SimpleFriendViewHolder(binding = binding)
    }

    class SimpleFriendViewHolder(val binding: ItemNewConversationBinding) : BaseViewHolder(binding.root) {

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



    class FriendsDiffCallback : DiffUtil.ItemCallback<FriendEntity>() {

        override fun areItemsTheSame(oldItem: FriendEntity, newItem: FriendEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FriendEntity, newItem: FriendEntity): Boolean {
            return oldItem == newItem
        }
    }
}