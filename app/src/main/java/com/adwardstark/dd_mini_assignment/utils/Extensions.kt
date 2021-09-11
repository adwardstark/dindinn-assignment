package com.adwardstark.dd_mini_assignment.utils

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.adwardstark.dd_mini_assignment.ui.MainActivity
import com.adwardstark.dd_mini_assignment.utils.Constants.DATE_FORMAT
import com.adwardstark.dd_mini_assignment.utils.Constants.TIME_FORMAT
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
    val sdf = SimpleDateFormat(DATE_FORMAT, Locale.US)
    val date = sdf.parse(timestamp)
    return SimpleDateFormat(TIME_FORMAT, Locale.US).format(date!!)
}

fun getExpiryAndAlertTime(expiryTimestamp: String, alertTimestamp: String)
        : Pair<Long, Long> {
    val sdf = SimpleDateFormat(DATE_FORMAT, Locale.US)
    val expiryTime: Long
    val alertTime: Long
    try {
        expiryTime = sdf.parse(expiryTimestamp)!!.time
        alertTime = sdf.parse(alertTimestamp)!!.time
    } catch (e: Throwable) {
        e.printStackTrace()
        return Pair(0,0)
    }
    val nowTime = Date().time

    val timeRemExpiryMin = (expiryTime - nowTime)
        .coerceAtLeast(0)
    val timeRemAlertMin = (alertTime - nowTime)
        .coerceAtLeast(0)

    return Pair(timeRemExpiryMin, timeRemAlertMin)
}

fun formatIntoMinsAndSeconds(seconds: Long): String {
    return when {
        seconds >= 120 -> {
            "${seconds/60} mins"
        }
        seconds in 60..119 -> {
            "${seconds/60} min"
        }
        else -> {
            "$seconds s"
        }
    }
}