package com.serhiymysyshyn.messme.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.serhiymysyshyn.messme.databinding.ItemChatBinding
import com.serhiymysyshyn.messme.domain.messages.MessageEntity
import kotlinx.android.synthetic.main.fragment_account.*

open class ChatsAdapter : MessagesAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemChatBinding.inflate(layoutInflater, parent, false)
        return ChatViewHolder(binding)
    }

    class ChatViewHolder(val binding: ItemChatBinding) : BaseViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onClick?.onClick(item, it)
            }
            binding.root.setOnLongClickListener{
                onClick?.onLongClick(item,it)
                true
            }
        }

        override fun onBind(item: Any) {
            (item as? MessageEntity)?.let {
                binding.message = it
            }
        }
    }
}