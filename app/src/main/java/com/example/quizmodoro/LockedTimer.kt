package com.example.quizmodoro
import android.content.Intent
import android.content.Context
import android.os.CountDownTimer
import android.widget.Toast
import android.util.Log
import com.example.quizmodoro.databinding.ActivityMainBinding
import com.example.quizmodoro.databinding.LockedLayoutBinding
import com.example.quizmodoro.databinding.ProcessTimerBinding

class LockedTimer(private val context: Context, private val binding: LockedLayoutBinding) {
    private var countDownTimer: CountDownTimer? = null
    private val initialTimeInMillis: Long = 60000 // 60 seconds

    init {
        startCountDownTimer(initialTimeInMillis)
    }

    private fun startCountDownTimer(remainingTime: Long) {
        Log.d("TAG", "is it inside 20")
        countDownTimer = object : CountDownTimer(remainingTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                updateTimerUI(millisUntilFinished)
            }

            override fun onFinish() {
                Log.d("tag", "finished locked timer")
                Toast.makeText(context, "Timer ended", Toast.LENGTH_SHORT).show()
                resetTimer()
            }
        }.start()
    }

    private fun resetTimer() {
        countDownTimer?.cancel()
        startCountDownTimer(initialTimeInMillis)
    }

    private fun updateTimerUI(timeInMillis: Long) {
        val minutes = (timeInMillis / 1000) / 60
        val seconds = (timeInMillis / 1000) % 60
        Log.d("tag", String.format("%02d:%02d", minutes, seconds))
        binding.lockedTimeLeft.text = String.format("%02d:%02d", minutes, seconds)
    }
}