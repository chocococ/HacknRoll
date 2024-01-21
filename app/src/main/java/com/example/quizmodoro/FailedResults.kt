package com.example.quizmodoro

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quizmodoro.databinding.FailedResultsBinding

class FailedResults : AppCompatActivity() {

    private lateinit var failedResultsBinding: FailedResultsBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            failedResultsBinding = FailedResultsBinding.inflate(layoutInflater)
            setContentView(failedResultsBinding.root)
            val score = intent.getIntExtra("SCORE", 0)
            failedResultsBinding.failedResults.text = "$score/10"

            failedResultsBinding.getLocked.setOnClickListener {
                val overlayIntent = Intent(this@FailedResults, OverlayService::class.java)
                startService(overlayIntent)
                finish()
            }
    }
}