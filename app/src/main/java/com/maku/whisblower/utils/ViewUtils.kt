package com.maku.whisblower.utils

import android.app.Activity
import android.view.View
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar


fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

 fun showError(v: EditText) {
     v.error = "Field cannot be open."
}

fun createSnack(
    ctx: Activity,
    txt: String,
    txtAction: String,
    isDefinate: Boolean,
    action: View.OnClickListener
) {
    Snackbar.make(ctx.findViewById<View>(android.R.id.content), txt, Snackbar.LENGTH_INDEFINITE)
        .setAction(txtAction, action)
        .show()
}