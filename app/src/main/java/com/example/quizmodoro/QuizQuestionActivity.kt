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
import com.example.quizmodoro.databinding.ActivityMainBinding
import com.example.quizmodoro.databinding.QuizQuestionsBinding

class QuizQuestionActivity : AppCompatActivity(), View.OnClickListener {
    private var currentPosition = 1 // to track which question we're on
    private lateinit var questionsList: ArrayList<Question> // the list of questions
    private lateinit var binding: QuizQuestionsBinding // Corrected binding class name
    private var mSelectedOptionPosition: Int = 0
    private var mAnswers: MutableList<Int> = mutableListOf() // to keep track of selected options
    private var mQuestionList: ArrayList<Question>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = QuizQuestionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //questionsList = fetchQuestions() // This function needs to be implemented to fetch questions
        setQuestion()
        setupClickListeners()

    }
    fun getQuestions() {
        mQuestionList = Constants.getQuestions()
    }
    private fun setupClickListeners() {
        binding.tvOptionOne.setOnClickListener(this)
        binding.tvOptionTwo.setOnClickListener(this)
        binding.tvOptionThree.setOnClickListener(this)
        binding.tvOptionFour.setOnClickListener(this)
        binding.btnNext.setOnClickListener {
            handleNextButtonClick()
        }
        binding.btnPrevious.setOnClickListener {
            handlePreviousButtonClick()
        }
        binding.btnSubmit.setOnClickListener {
            finishQuiz()
        }

    }

    private fun saveAnswer() {
        if (mSelectedOptionPosition != 0) {
            // Save the selected option
            if (currentPosition < mAnswers.size) {
                mAnswers[currentPosition] = mSelectedOptionPosition
            } else {
                mAnswers.add(mSelectedOptionPosition)
            }
            mSelectedOptionPosition = 0 // Reset for the next question
        }
    }


    private fun resetOptionsBackground() {
        val options = ArrayList<TextView>()
        options.add(binding.tvOptionOne)
        options.add(binding.tvOptionTwo)
        options.add(binding.tvOptionThree)
        options.add(binding.tvOptionFour)

        for (option in options) {
            option.setBackgroundResource(R.drawable.default_option_border)
            option.setTextColor(Color.parseColor("#7A8089")) // Reset the text color
            option.typeface = Typeface.DEFAULT // Reset the typeface to default
        }
    }


    private fun setQuestion() {
        questionsList = generateDummyQuestions() // Use the function to generate questions
        val question = questionsList[currentPosition - 1]
        binding.tvQuestion.text = question.question
        // Optionally set image if you have one
        binding.tvOptionOne.text = question.optionOne
        binding.tvOptionTwo.text = question.optionTwo
        binding.tvOptionThree.text = question.optionThree
        binding.tvOptionFour.text = question.optionFour

        // Update the visibility of the Next/Submit button.
        binding.btnNext.visibility = if (currentPosition == questionsList.size - 1) View.INVISIBLE else View.VISIBLE
        binding.btnSubmit.visibility = if (currentPosition == questionsList.size - 1) View.VISIBLE else View.INVISIBLE
        binding.btnPrevious.visibility = if (currentPosition == 0) View.INVISIBLE else View.VISIBLE

        // Reset selected option view
        mSelectedOptionPosition = mAnswers.getOrNull(currentPosition) ?: 0
        resetOptionsBackground()
        highlightSelectedOption()
    }

    private fun highlightSelectedOption() {
        if (mSelectedOptionPosition != 0) {
            // Highlight the previously selected option
            val tvSelected = when (mSelectedOptionPosition) {
                1 -> binding.tvOptionOne
                2 -> binding.tvOptionTwo
                3 -> binding.tvOptionThree
                4 -> binding.tvOptionFour
                else -> null
            }
            tvSelected?.let {
                selectedOptionView(it, mSelectedOptionPosition)
            }
        }
    }






    private fun generateDummyQuestions(): ArrayList<Question> {
        val questions = ArrayList<Question>()
        questions.add(Question(
            id = 1,
            question = "What is the capital of France?",
             // Assuming no image for now
            optionOne = "New York",
            optionTwo = "London",
            optionThree = "Paris",
            optionFour = "Berlin",
            correctAnswer = 3
        ))
        questions.add(Question(
            id = 2,
            question = "Which famous scientist developed the theory of relativity?",

            optionOne = "Isaac Newton",
            optionTwo = "Galileo Galilei",
            optionThree = "Albert Einstein",
            optionFour = "Niels Bohr",
            correctAnswer = 3
        ))

        // Question Set 3
        questions.add(Question(
            id = 3,
            question = "What is the capital of Japan?",

            optionOne = "Beijing",
            optionTwo = "Seoul",
            optionThree = "Tokyo",
            optionFour = "Bangkok",
            correctAnswer = 3
        ))
        // Add more questions similarly...
        return questions
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_optionOne -> {
                selectedOptionView(binding.tvOptionOne, 1)
            }
            R.id.tv_optionTwo -> {
                selectedOptionView(binding.tvOptionTwo, 2)
            }
            R.id.tv_optionThree -> {
                selectedOptionView(binding.tvOptionThree, 3)
            }
            R.id.tv_optionFour -> {
                selectedOptionView(binding.tvOptionFour, 4)
            }
            // Handle other clicks if necessary (e.g., submit button)
            R.id.btnNext -> handleNextButtonClick()
            R.id.btnPrevious -> handlePreviousButtonClick()
            R.id.btnSubmit -> finishQuiz()
        }
    }
    private fun handleNextButtonClick() {
        if (mSelectedOptionPosition == 0) {
            // No option selected, show a prompt
            Toast.makeText(this, "Please select an option", Toast.LENGTH_SHORT).show()
        } else {
            saveAnswer()
            if (currentPosition < questionsList.size - 1) {
                currentPosition++
                setQuestion()
            } else {
                // Last question, should not happen as Next is invisible, but safe to handle
                binding.btnSubmit.performClick()
                Toast.makeText(this,"Congratulation you made to the end",Toast.LENGTH_LONG).show()
                val intent = Intent(this,ResultActivity::class.java)
                intent.putExtra(Constants.USER_NAME, "abc")
                intent.putExtra(Constants.CORRECT_ANSWER, "mCorrectAnswer")
                intent.putExtra(Constants.TOTAL_QUESTIONS, mQuestionList?.size)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun handlePreviousButtonClick() {
        if (currentPosition > 0) {
            saveAnswer()
            currentPosition--
            setQuestion()
        }
    }


    private fun finishQuiz() {
        var score = 0
        for (i in questionsList.indices) {
            if (questionsList[i].correctAnswer == mAnswers.getOrNull(i)) {
                score++
            }
        }
        // Create an intent that launches the ResultActivity
        Toast.makeText(this,"Congratulation you made to the end",Toast.LENGTH_LONG).show()
        val intent = Intent(this, ResultActivity::class.java)

        intent.putExtra(Constants.USER_NAME, "abc")
        intent.putExtra(Constants.CORRECT_ANSWER, "mCorrectAnswer")
        intent.putExtra(Constants.TOTAL_QUESTIONS, mQuestionList?.size)
        startActivity(intent)
        finish()

    }

    private fun navigateToResult() {
        val intent = Intent(this@QuizQuestionActivity, ResultActivity::class.java)
        startActivity(intent)
    }


    private fun selectedOptionView(tv: TextView, selectedOptionNum: Int) {
        resetOptionsBackground() // Reset background of all options
        mSelectedOptionPosition = selectedOptionNum // Save the selected option position
        tv.setTextColor(Color.parseColor("#363A43"))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(this, R.drawable.selected_option_border)
    }




}