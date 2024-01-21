package com.example.quizmodoro

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.quizmodoro.databinding.FailedResultsBinding

class FailedResults : AppCompatActivity() {

    private lateinit var failedResultsBinding: FailedResultsBinding
    private lateinit var questionsList: ArrayList<Question> // the list of questions
    private lateinit var mAnswers: ArrayList<Int> // to keep track of selected options
    private var currentPosition = 0 // to track which question we're on

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

        questionsList = intent.getParcelableArrayListExtra<Question>("questions") as ArrayList<Question>
        mAnswers = intent.getIntegerArrayListExtra("answers")!!

        setupClickListeners()
        setQuestion()

    }


    private fun setupClickListeners() {
        failedResultsBinding.btnNext.setOnClickListener {
            handleNextButtonClick()
        }
        failedResultsBinding.btnPrevious.setOnClickListener {
            handlePreviousButtonClick()
        }

    }

    private fun resetOptionsBackground() {
        val options = ArrayList<TextView>()
        options.add(failedResultsBinding.tvOptionOne)
        options.add(failedResultsBinding.tvOptionTwo)
        options.add(failedResultsBinding.tvOptionThree)
        options.add(failedResultsBinding.tvOptionFour)

        for (option in options) {
            option.setBackgroundResource(R.drawable.default_option_border)
            option.setTextColor(Color.parseColor("#7A8089")) // Reset the text color
            option.typeface = Typeface.DEFAULT // Reset the typeface to default
        }
    }
    private fun setQuestion() {

//        questionsList = generateDummyQuestions() // Use the function to generate questions
        resetOptionsBackground()
        val question = questionsList[currentPosition]
        failedResultsBinding.tvQuestion.text = question.question
        // Optionally set image if you have one
        failedResultsBinding.tvOptionOne.text = question.optionOne
        failedResultsBinding.tvOptionTwo.text = question.optionTwo
        failedResultsBinding.tvOptionThree.text = question.optionThree
        failedResultsBinding.tvOptionFour.text = question.optionFour

        if (mAnswers[currentPosition] != question.correctAnswer) {
            answerView(mAnswers[currentPosition], R.drawable.wrong_option_border)
        }

        answerView(question.correctAnswer, R.drawable.correct_option_border)

        // Update the visibility of the Next/Submit button.
        failedResultsBinding.btnNext.visibility = if (currentPosition == questionsList.size - 1) View.INVISIBLE else View.VISIBLE
        failedResultsBinding.btnPrevious.visibility = if (currentPosition == 0) View.INVISIBLE else View.VISIBLE

    }

    private fun answerView(answer: Int, drawableView: Int) {
        when (answer) {
            1 -> {
                failedResultsBinding.tvOptionOne.background = ContextCompat.getDrawable(this,drawableView)
            }
            2 -> {
                failedResultsBinding.tvOptionTwo.background = ContextCompat.getDrawable(this,drawableView)
            }
            3 -> {
                failedResultsBinding.tvOptionThree.background = ContextCompat.getDrawable(this,drawableView)
            }
            4 -> {
                failedResultsBinding.tvOptionFour.background = ContextCompat.getDrawable(this,drawableView)
            }
        }
    }
    private fun handleNextButtonClick() {
        if (currentPosition < questionsList.size - 1) {
            currentPosition++
            setQuestion()
        } else {
            // Last question, should not happen as Next is invisible, but safe to handle
            Toast.makeText(this,"Congratulation you made to the end", Toast.LENGTH_LONG).show()
            val intent = Intent(this,ResultActivity::class.java)
            intent.putExtra(Constants.USER_NAME, "abc")
            intent.putExtra(Constants.CORRECT_ANSWER, "mCorrectAnswer")
            startActivity(intent)
            finish()
        }
    }

    private fun handlePreviousButtonClick() {
        if (currentPosition > 0) {
            currentPosition--
            setQuestion()
        }
    }
}