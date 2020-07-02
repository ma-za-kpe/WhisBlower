package com.maku.whisblower.utils

import android.view.View
import android.widget.EditText


fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

 fun showError(v: EditText) {
     v.error = "Field cannot be open."
}