package com.github.almasud.e_shop.ui.util

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

object ImageUtil {
    private const val TAG = "ImageUtils"

    fun setImageLinkWithTextView(
        context: Context,
        imageLink: String,
        name: String?,
        imageView: ImageView,
        textView: TextView,
        onImageLoadStatus: ((Boolean) -> Unit)? = null
    ) {
        setImageWithTextView(context, imageLink, name, imageView, textView, onImageLoadStatus)
    }

    fun setImageBitmapWithTextView(
        context: Context,
        bitmap: Bitmap,
        name: String?,
        imageView: ImageView,
        textView: TextView,
        onImageLoadStatus: ((Boolean) -> Unit)? = null
    ) {
        setImageWithTextView(context, bitmap, name, imageView, textView, onImageLoadStatus)
    }

    fun setImageDrawableWithTextView(
        context: Context,
        drawable: Drawable,
        name: String?,
        imageView: ImageView,
        textView: TextView,
        onImageLoadStatus: ((Boolean) -> Unit)? = null
    ) {
        setImageWithTextView(context, drawable, name, imageView, textView, onImageLoadStatus)
    }

    private fun setImageWithTextView(
        context: Context,
        imageObject: Any,
        name: String?,
        imageView: ImageView,
        textView: TextView,
        onImageLoadStatus: ((Boolean) -> Unit)? = null
    ) {
        if (name != null) {
            textView.text = UIUtil.getFirstOne(name)
        } else textView.text = "U"
        textView.visibility = View.VISIBLE
        imageView.visibility = View.INVISIBLE
        Glide.with(context)
            .load(imageObject)
//            .diskCacheStrategy(DiskCacheStrategy.NONE)
//            .skipMemoryCache(true)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    any: Any,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.e(TAG, "onLoadFailed: exception: " + e!!.message, e)
                    onImageLoadStatus?.let { it(false) }
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    any: Any,
                    target: Target<Drawable?>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.i(TAG, "onResourceReady: is called")
                    onImageLoadStatus?.let { it(true) }
                    textView.visibility = View.INVISIBLE
                    imageView.visibility = View.VISIBLE
                    // Return true if want to handle things like animations ourself and false if want glide to handle them for us.
                    return false
                }
            })
            .into(imageView)
    }
}