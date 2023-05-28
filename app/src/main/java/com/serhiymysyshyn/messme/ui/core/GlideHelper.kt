package com.serhiymysyshyn.messme.ui.core

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.serhiymysyshyn.messme.R
import com.serhiymysyshyn.messme.remote.service.ServiceFactory

object GlideHelper {
    fun loadImage(context: Context, path: String?, iv: ImageView, placeholder: Drawable? = iv.drawable) {
        val imgPath = ServiceFactory.SERVER_URL + path?.replace("..", "")

        Glide.with(context)
            .load(imgPath)
            .placeholder(placeholder)
            .error(placeholder)
            .into(iv)
    }

    fun loadSvgImage(requireActivity: Activity, imageUrl: Int, iv: ImageView){
        Glide.with(requireActivity)
            .load(imageUrl)
            .placeholder(R.color.black)
            .error(R.color.black)
            .into(iv)
    }

    fun loadImage(context: Context, path: String?, iv: ImageView, placeholder: Int) {
        loadImage(context, path, iv, context.resources.getDrawable(placeholder))
    }

    @JvmStatic
    @BindingAdapter("profileImage")
    fun ImageView.loadImage(image: String?) {
        loadImage(this.context, image, this, R.drawable.ic_account_circle)
    }

    @JvmStatic
    @BindingAdapter("messageImage")
    fun ImageView.loadMessageImage(message: String?) {
        if (message.isNullOrBlank()) return
        loadImage(this.context, message, this, R.drawable.placeholder)
    }
}
