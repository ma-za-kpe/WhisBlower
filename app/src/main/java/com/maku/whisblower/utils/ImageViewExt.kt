package com.maku.whisblower.utils

import android.view.View
import android.widget.ImageView
import com.maku.whisblower.R

fun ImageView.setImageFromUrlWithProgressBar(url: String, progress: View) {
    GlideApp.with(this.context)
        .load(url)
        .thumbnail(0.1f)
        .placeholder(R.drawable.siki)
        .listener(PhotosRequestListener(progress))
        .into(this)
}