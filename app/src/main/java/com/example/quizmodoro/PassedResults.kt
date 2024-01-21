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
import com.example.quizmodoro.databinding.ResultsBinding

class PassedResults : AppCompatActivity() {
    private lateinit var passedResultsBinding: ResultsBinding
    private lateinit var questionsList: ArrayList<Question> // the list of questions
    private lateinit var mAnswers: ArrayList<Int> // to keep track of selected options
    private var currentPosition = 0 // to track which question we're on

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

        questionsList = intent.getParcelableArrayListExtra<Question>("questions") as ArrayList<Question>
        mAnswers = intent.getIntegerArrayListExtra("answers")!!

        setupClickListeners()
        setQuestion()
    }

    private fun setupClickListeners() {
        passedResultsBinding.btnNext.setOnClickListener {
            handleNextButtonClick()
        }
        passedResultsBinding.btnPrevious.setOnClickListener {
            handlePreviousButtonClick()
        }

    }

    private fun resetOptionsBackground() {
        val options = ArrayList<TextView>()
        options.add(passedResultsBinding.tvOptionOne)
        options.add(passedResultsBinding.tvOptionTwo)
        options.add(passedResultsBinding.tvOptionThree)
        options.add(passedResultsBinding.tvOptionFour)

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
        passedResultsBinding.tvQuestion.text = question.question
        // Optionally set image if you have one
        passedResultsBinding.tvOptionOne.text = question.optionOne
        passedResultsBinding.tvOptionTwo.text = question.optionTwo
        passedResultsBinding.tvOptionThree.text = question.optionThree
        passedResultsBinding.tvOptionFour.text = question.optionFour

        if (mAnswers[currentPosition] != question.correctAnswer) {
            answerView(mAnswers[currentPosition], R.drawable.wrong_option_border)
        }

        answerView(question.correctAnswer, R.drawable.correct_option_border)

        // Update the visibility of the Next/Submit button.
        passedResultsBinding.btnNext.visibility = if (currentPosition == questionsList.size - 1) View.INVISIBLE else View.VISIBLE
        passedResultsBinding.btnPrevious.visibility = if (currentPosition == 0) View.INVISIBLE else View.VISIBLE

    }

    private fun answerView(answer: Int, drawableView: Int) {
        when (answer) {
            1 -> {
                passedResultsBinding.tvOptionOne.background = ContextCompat.getDrawable(this,drawableView)
            }
            2 -> {
                passedResultsBinding.tvOptionTwo.background = ContextCompat.getDrawable(this,drawableView)
            }
            3 -> {
                passedResultsBinding.tvOptionThree.background = ContextCompat.getDrawable(this,drawableView)
            }
            4 -> {
                passedResultsBinding.tvOptionFour.background = ContextCompat.getDrawable(this,drawableView)
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
