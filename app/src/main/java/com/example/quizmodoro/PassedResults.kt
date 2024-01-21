package com.example.quizmodoro

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quizmodoro.databinding.ResultsBinding

class PassedResults : AppCompatActivity() {
    private lateinit var passedResultsBinding: ResultsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        passedResultsBinding = ResultsBinding.inflate(layoutInflater)
        setContentView(passedResultsBinding.root)

        val score = intent.getIntExtra("SCORE", 0)
        passedResultsBinding.passedResults.text = "$score/10"

        passedResultsBinding.startAgain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
