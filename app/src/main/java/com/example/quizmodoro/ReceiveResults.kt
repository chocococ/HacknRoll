package com.example.quizmodoro


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quizmodoro.OverlayService
import com.example.quizmodoro.databinding.ActivityMainBinding
import com.example.quizmodoro.databinding.FailedResultsBinding
import com.example.quizmodoro.databinding.ResultsBinding
import com.example.quizmodoro.databinding.SessionEndMainBinding

class ReceiveResults : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var resultsBinding: ResultsBinding
    private lateinit var failedResultsBinding: FailedResultsBinding

    var results = 4;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (results < 5){
            failedResultsBinding = FailedResultsBinding.inflate(layoutInflater)
            setContentView(failedResultsBinding.root)
        } else {
            resultsBinding = ResultsBinding.inflate(layoutInflater)
            setContentView(resultsBinding.root)
        }





    }
}