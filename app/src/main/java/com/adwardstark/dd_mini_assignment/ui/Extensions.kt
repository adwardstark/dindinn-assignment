package com.adwardstark.dd_mini_assignment.ui

import android.widget.Toast
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Aditya Awasthi on 08/09/21.
 * @author github.com/adwardstark
 */
fun Fragment.showHomeUp(enabled: Boolean) {
    if(this.activity is MainActivity) {
        (this.activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(enabled)
    }
}

fun Fragment.showToast(message: String) {
    Toast.makeText(this.requireContext(), message, Toast.LENGTH_SHORT).show()
}

// 2021-06-10T15:05+00Z
fun getTimeFrom(timestamp: String): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'", Locale.US)
    val date = sdf.parse(timestamp)
    return SimpleDateFormat("HH:mm aa", Locale.US).format(date!!)
}