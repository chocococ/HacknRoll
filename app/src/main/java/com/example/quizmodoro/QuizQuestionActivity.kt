package com.example.quizmodoro

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quizmodoro.databinding.QuizQuestionsBinding

class QuizQuestionActivity : AppCompatActivity() {
    private var currentPosition = 1 // to track which question we're on
    private lateinit var questionsList: ArrayList<Question> // the list of questions
    private lateinit var binding: QuizQuestionsBinding // Corrected binding class name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = QuizQuestionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //questionsList = fetchQuestions() // This function needs to be implemented to fetch questions
        setQuestion()
    }

    private fun setQuestion() {
//        val question = questionsList[currentPosition - 1]
//        binding.tvQuestion.text = question.question
        // set image if you have one
        // set options
        // set progress bar
        // etc.
    }

}