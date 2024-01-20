package com.example.quizmodoro
import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent


import android.provider.Settings

class AccessibilityClass: AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.let {
            if (it.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                // Check if the user has switched away from your app
                if (!isYourApp(it.packageName.toString())) {
                    // Bring the user back to your app
                    bringAppToFront()
                }
            }
        }
    }

    override fun onInterrupt() {
        // Handle interruptions
    }

    private fun isYourApp(packageName: String): Boolean {
        // Implement logic to check if the current app is your app
        return packageName == "com.example.quizmodoro"
    }

    private fun bringAppToFront() {
        // Implement logic to bring your app to the foreground
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}