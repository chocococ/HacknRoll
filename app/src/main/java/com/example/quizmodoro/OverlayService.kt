package com.example.quizmodoro

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.WindowManager
import android.graphics.PixelFormat
import com.example.quizmodoro.databinding.LockedLayoutBinding
import android.content.Context
import android.os.CountDownTimer
import android.widget.Toast
import android.util.Log

class OverlayService : Service() {
    private lateinit var windowManager: WindowManager
    private lateinit var binding: LockedLayoutBinding
    private var countDownTimer: CountDownTimer? = null
    private val initialTimeInMillis: Long = 60000 // 60 seconds

    override fun onBind(intent: Intent): IBinder? {
        // Return null as this is not a bound service
        return null
    }

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        // Inflate the layout using View Binding
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = LockedLayoutBinding.inflate(inflater)

        // Define layout parameters for the overlay view
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, // Ensure this is used for overlay
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.RGBA_1010102
        )

        // Add the overlay view to the WindowManager
        windowManager.addView(binding.root, params)

        // Schedule to remove the overlay after a certain period
        Handler(Looper.getMainLooper()).postDelayed({
            windowManager.removeView(binding.root)
            stopSelf()
        }, initialTimeInMillis) // Remove after 60 seconds

        startCountDownTimer(initialTimeInMillis)
    }

    private fun startCountDownTimer(remainingTime: Long) {
        countDownTimer = object : CountDownTimer(remainingTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                updateTimerUI(millisUntilFinished)
            }

            override fun onFinish() {
                Toast.makeText(this@OverlayService, "Timer ended", Toast.LENGTH_SHORT).show()
                stopSelf()
            }
        }.start()
    }

    private fun updateTimerUI(timeInMillis: Long) {
        val minutes = (timeInMillis / 1000) / 60
        val seconds = (timeInMillis / 1000) % 60
        binding.lockedTimeLeft.text = String.format("%02d:%02d", minutes, seconds)
    }
}
