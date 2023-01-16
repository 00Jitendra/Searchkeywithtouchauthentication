package com.example.searchkeywithtouchauthentication.utils

/**
 * Extension functions and Binding Adapters.
 */

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast


fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.notifyUser(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}


