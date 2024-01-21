package com.example.quizmodoro

import android.app.Activity
import android.content.Intent
import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.quizmodoro.databinding.ActivityMainBinding
import com.example.quizmodoro.databinding.ProcessTimerBinding
import android.provider.Settings
import com.example.quizmodoro.UploadActivity
import com.example.quizmodoro.databinding.SessionEndMainBinding


import com.example.quizmodoro.databinding.LockedLayoutBinding
import com.example.quizmodoro.OverlayService
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.net.Uri

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var timerBinding: ProcessTimerBinding
    private var countDownTimer: CountDownTimer? = null
    private var initialTimeInMillis: Long = 0
    private var remainingTimeInMillis: Long = 0
    companion object {
        private lateinit var adminEnableResultLauncher: ActivityResultLauncher<Intent>

    }
    private fun openOverlaySettings(context: Context) {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.packageName))
        context.startActivity(intent)
    }

    private fun isOverlayEnabled(context: Context): Boolean {
        return Settings.canDrawOverlays(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//
//        val deviceManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
//        val lockDeviceFunction = ComponentName(this, AdminReceiver::class.java)
//        if (!deviceManager.isAdminActive(lockDeviceFunction)) {
//            initialiseAdminPrivileges()
//        }
        if (!isOverlayEnabled(this)) {
            openOverlaySettings(this)
        }

        emptyTimer()
    }



    private fun emptyTimer() {
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.startButton.setOnClickListener {
            val timeText = mainBinding.timeSelection.text.toString()

            if (timeText.isNotBlank()) {
                val convertedTime = convertToTime(timeText)
                initialTimeInMillis = convertedTime * 1000
                remainingTimeInMillis = initialTimeInMillis



                timerBinding = ProcessTimerBinding.inflate(layoutInflater)
                setContentView(timerBinding.root)
                startCountDownTimer(initialTimeInMillis)

                Toast.makeText(this, "Timer started for $convertedTime seconds", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_SHORT).show()
            }
        }
    }

//    private fun initialiseAdminPrivileges() {
//        val lockDeviceFunction = ComponentName(this, AdminReceiver::class.java)
//        adminEnableResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                Toast.makeText(this, "Admin privileges granted", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(this, "Admin privileges not granted", Toast.LENGTH_SHORT).show()
//            }
//        }
//        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
//            val text: String = "This app requires permission to lock your phone during the indicated pomodoro session."
//            putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, lockDeviceFunction)
//            putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, text)
//        }
//        adminEnableResultLauncher.launch(intent)
//    }




    private fun startCountDownTimer(remainingTime: Long) {

        timerBinding.stopButton.setOnClickListener {
            stopTimer()
        }

        timerBinding.resetButton.setOnClickListener {
            resetTimer()
        }

        countDownTimer = object : CountDownTimer(remainingTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerBinding.stopButton.isEnabled = true
                timerBinding.resumeButton.isEnabled = false

                remainingTimeInMillis = millisUntilFinished
                updateTimerUI(millisUntilFinished)
                trackProgressBar()
//                val deviceManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
//                deviceManager.lockNow()
            }
            override fun onFinish() {
                Toast.makeText(this@MainActivity, "Pomodoro session ended", Toast.LENGTH_SHORT).show()
//                val overlayIntent = Intent(this@MainActivity, OverlayService::class.java)
//                startService(overlayIntent)
//                endSessionBinding = SessionEndMainBinding.inflate(layoutInflater)
//                setContentView(endSessionBinding.root)

                val intent = Intent(this@MainActivity, UploadActivity::class.java)
                startActivity(intent)
//                navigateToQuiz()

                resetTimer()

//                val lockedTimerBinding = LockedLayoutBinding.inflate(layoutInflater)
//                setContentView(lockedTimerBinding.root)
//                val lockedTimer = LockedTimer(this@MainActivity, lockedTimerBinding)

            }
        }.start()
    }
    private fun navigateToQuiz() {
        val intent = Intent(this, QuizQuestionActivity::class.java)
        startActivity(intent)
    }
    private fun stopTimer() {
        countDownTimer?.cancel()
        updateTimerUI(remainingTimeInMillis)

        timerBinding.stopButton.isEnabled = false
        timerBinding.resumeButton.isEnabled = true

        timerBinding.resumeButton.setOnClickListener {
            startCountDownTimer(remainingTimeInMillis)
        }
    }

    private fun resetTimer() {
        countDownTimer?.cancel()
        emptyTimer()
    }

    private fun convertToTime(stringTime: String): Long {
        val minutes = stringTime.split(":").toTypedArray()[0];
        val seconds = stringTime.split(":").toTypedArray()[1];

        val totalTime = (minutes.toLong()*60)+seconds.toLong();
        return totalTime;
    }






    private fun trackProgressBar() {
        val progress = ((initialTimeInMillis - remainingTimeInMillis).toFloat() / initialTimeInMillis * 100).toInt()
        timerBinding.progressBar.progress = progress
    }

    private fun updateTimerUI(timeInMillis: Long) {
        remainingTimeInMillis = timeInMillis
        val minutes = (timeInMillis / 1000) / 60
        val seconds = (timeInMillis / 1000) % 60
        timerBinding.timeLeft.text = String.format("%02d:%02d", minutes, seconds)
        trackProgressBar()
    }




}

