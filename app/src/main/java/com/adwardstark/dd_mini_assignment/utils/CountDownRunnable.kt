package com.adwardstark.dd_mini_assignment.utils

import android.annotation.SuppressLint
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.TextView
import com.adwardstark.dd_mini_assignment.utils.Constants.ONE_SECOND
import com.google.android.material.button.MaterialButton

/**
 * Created by Aditya Awasthi on 09/09/21.
 * @author github.com/adwardstark
 */
class CountDownRunnable(private val handler: Handler): Runnable {

    private var _expiresIn: Long = 0
    private var _alertIn: Long = 0
    private lateinit var _timeLeftView: TextView
    private lateinit var _autoRejectView: TextView
    private lateinit var _acceptButton: MaterialButton

    companion object {
        private val TAG = CountDownRunnable::class.java.simpleName
    }

    fun setElapsed(expiryMillis: Long, alertMillis: Long) {
        _expiresIn = expiryMillis
        _alertIn = alertMillis/ONE_SECOND
    }

    fun setViews(timeLeftView: TextView, autoRejectView: TextView, acceptButton: MaterialButton) {
        _timeLeftView = timeLeftView
        _autoRejectView = autoRejectView
        _acceptButton = acceptButton
    }

    @SuppressLint("SetTextI18n")
    override fun run() {
        if(_expiresIn >= 0) {
            val secondsRemaining = _expiresIn/ONE_SECOND
            _timeLeftView.text = formatIntoMinsAndSeconds(secondsRemaining)

            if(_alertIn == secondsRemaining) {
                Log.d(TAG, "->alert()")
            }

            _expiresIn -= 1000
            handler.postDelayed(this, ONE_SECOND)
        } else {
            Log.d(TAG, "->expired()")
            _autoRejectView.visibility = View.INVISIBLE
            _timeLeftView.visibility = View.INVISIBLE
            _acceptButton.text = "Expired"
        }
    }
}