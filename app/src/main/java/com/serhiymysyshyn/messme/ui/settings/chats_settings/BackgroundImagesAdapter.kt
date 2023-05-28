package com.serhiymysyshyn.messme.ui.settings.chats_settings

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.serhiymysyshyn.messme.R
import com.serhiymysyshyn.messme.domain.friends.FriendEntity
import com.serhiymysyshyn.messme.ui.core.BaseAdapter
import com.serhiymysyshyn.messme.ui.core.GlideHelper
import kotlinx.android.synthetic.main.fragment_messages.*

open class BackgroundImagesAdapter(context: Activity) : RecyclerView.Adapter<BackgroundImagesAdapter.BackgroundImagesViewHolder>() {

    private var data: List<Int> = listOf()
    private var clickListener: OnItemClickListener? = null
    private var _context = context


    inner class BackgroundImagesViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val itemImage : ImageView = itemView.findViewById(R.id.itemImg)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                clickListener?.onItemClick(adapterPosition)
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

//    fun updateAdapter(_data:List<Int>, _clickListener:OnItemClickListener){
//        data = _data
//        clickListener = _clickListener
//    }

    fun updateAdapter(_data:List<Int>/*, _clickListener:OnItemClickListener*/){
        data = _data
        //clickListener = _clickListener
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): BackgroundImagesViewHolder {
        val itemView = LayoutInflater.from(p0.context).inflate(R.layout.item_pick_background_img, p0, false)
        return BackgroundImagesViewHolder(itemView)
    }


    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: BackgroundImagesViewHolder, position: Int) {
        GlideHelper.loadSvgImage(_context, data[position], holder.itemImage)
    }

}
