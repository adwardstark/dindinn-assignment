package com.adwardstark.dd_mini_assignment.utils

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.TextView
import com.adwardstark.dd_mini_assignment.ui.customview.ProgressStepBar
import com.adwardstark.dd_mini_assignment.utils.Constants.ONE_SECOND
import com.google.android.material.button.MaterialButton

/**
 * Created by Aditya Awasthi on 09/09/21.
 * @author github.com/adwardstark
 */
class CountDownRunnable(private val context: Context, private val handler: Handler): Runnable {

    private var _expiresIn: Long = 0
    private var _alertIn: Long = 0
    private lateinit var _timeLeftView: TextView
    private lateinit var _autoRejectView: TextView
    private lateinit var _acceptButton: MaterialButton
    private lateinit var _progressBar: ProgressStepBar

    companion object {
        private val TAG = CountDownRunnable::class.java.simpleName
    }

    fun setElapsed(expiryMillis: Long, alertMillis: Long) {
        _expiresIn = expiryMillis
        _alertIn = alertMillis/ONE_SECOND
    }

    fun setViews(timeLeftView: TextView, autoRejectView: TextView,
                 acceptButton: MaterialButton, progressBar: ProgressStepBar) {
        _timeLeftView = timeLeftView
        _autoRejectView = autoRejectView
        _acceptButton = acceptButton
        _progressBar = progressBar
    }

    private fun updateProgressBar(seconds: Long) {
        when {
            seconds >= 240 -> { // Above 4 min
                _progressBar.step = 5
            }
            seconds in 180..239 -> { // Above 3 min, below 4 min
                _progressBar.step = 4
            }
            seconds in 120..179 -> { // Above 2 min, below 3 min
                _progressBar.step = 3
            }
            seconds in 60..119 -> { // Above 1 min, below 2 min
                _progressBar.step = 2
            }
            seconds in 1..59 -> { // Below 60 seconds
                _progressBar.step = 1
            } else -> {
                _progressBar.step = 0 // When 0
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun run() {
        if(_expiresIn >= 0) {
            val secondsRemaining = _expiresIn/ONE_SECOND
            _timeLeftView.text = formatIntoMinsAndSeconds(secondsRemaining)

            updateProgressBar(secondsRemaining)
            if(_alertIn == secondsRemaining) {
                Log.d(TAG, "->alert()")
                val alertPlayer = MediaPlayer
                    .create(context, Settings.System.DEFAULT_ALARM_ALERT_URI)
                alertPlayer.start()
                handler.postDelayed({
                    alertPlayer.stop()
                    alertPlayer.reset()
                    alertPlayer.release()
                }, 5000) // Stop alert in 5 seconds*/
            }

            _expiresIn -= ONE_SECOND
            handler.postDelayed(this, ONE_SECOND)
        } else {
            Log.d(TAG, "->expired()")
            _autoRejectView.visibility = View.INVISIBLE
            _timeLeftView.visibility = View.INVISIBLE
            _acceptButton.text = "Expired"
        }
    }
}