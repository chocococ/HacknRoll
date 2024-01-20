package com.example.quizmodoro

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.quizmodoro.databinding.QuizQuestionsBinding

class QuizQuestionActivity : AppCompatActivity(), View.OnClickListener {
    private var currentPosition = 1 // to track which question we're on
    private lateinit var questionsList: ArrayList<Question> // the list of questions
    private lateinit var binding: QuizQuestionsBinding // Corrected binding class name
    private var mSelectedOptionPosition: Int = 0
    private var mAnswers: MutableList<Int> = mutableListOf() // to keep track of selected options

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = QuizQuestionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //questionsList = fetchQuestions() // This function needs to be implemented to fetch questions
        setQuestion()
        setupClickListeners()
    }
    private fun setupClickListeners() {
        binding.tvOptionOne.setOnClickListener(this)
        binding.tvOptionTwo.setOnClickListener(this)
        binding.tvOptionThree.setOnClickListener(this)
        binding.tvOptionFour.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)
        binding.btnNext.setOnClickListener {
            if (currentPosition < questionsList.size - 1) {
                saveAnswer()
                currentPosition++
                setQuestion()
            } else {
                saveAnswer()
                finishQuiz()
            }
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
            // Reset other visual properties if needed
        }
    }


//    private fun setQuestion() {
//        questionsList = generateDummyQuestions() // Use the function to generate questions
//        val question = questionsList[currentPosition - 1]
//        binding.tvQuestion.text = question.question
//        // Optionally set image if you have one
//        binding.tvOptionOne.text = question.optionOne
//        binding.tvOptionTwo.text = question.optionTwo
//        binding.tvOptionThree.text = question.optionThree
//        binding.tvOptionFour.text = question.optionFour
//        resetOptionsBackground()
//        // You might want to update a progress bar or question number display here
//    }

//    private fun setQuestion() {
//        if (currentPosition < questionsList.size) {
//            val question = questionsList[currentPosition]
//            // Set question data to views...
//            updateButtons()
//        } else {
//            // End of quiz logic, show result or navigate to a result activity
//        }
//    }
private fun setQuestion() {
    questionsList = generateDummyQuestions()
    if (currentPosition < questionsList.size) {
        val question = questionsList[currentPosition]
        // Update UI with question data
        // ...

        // Reset previously selected options
        mSelectedOptionPosition = mAnswers.getOrNull(currentPosition) ?: 0
        resetOptionsBackground()
        highlightSelectedOption()
    }
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


    private fun updateButtons() {
        binding.btnSubmit.text = if (currentPosition == questionsList.size - 1) "Finish" else "Next"
        binding.btnPrevious.visibility = if (currentPosition == 0) View.INVISIBLE else View.VISIBLE

        // Set selected option if any
        val selectedOption = mAnswers.getOrNull(currentPosition)
        selectedOption?.let { selectedOptionView(findViewById(it), it) }
    }


    private fun generateDummyQuestions(): ArrayList<Question> {
        val questions = ArrayList<Question>()
        questions.add(Question(
            id = 1,
            question = "What is the capital of France?",
            image = null, // Assuming no image for now
            optionOne = "New York",
            optionTwo = "London",
            optionThree = "Paris",
            optionFour = "Berlin",
            correctAnswer = 3
        ))
        questions.add(Question(
            id = 2,
            question = "Which famous scientist developed the theory of relativity?",
            image = null,
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
            image = null,
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
            R.id.btnSubmit -> {
                if (mSelectedOptionPosition != 0) {
                    // Save the selected option
                    if (currentPosition < mAnswers.size) {
                        mAnswers[currentPosition] = mSelectedOptionPosition
                    } else {
                        mAnswers.add(mSelectedOptionPosition)
                    }
                }
                if (currentPosition < questionsList.size - 1) {
                    currentPosition++
                    setQuestion()
                } else {
                    // Finish the quiz and show results
                    finishQuiz()
                }
            }
            R.id.btnPrevious -> {
                if (currentPosition > 0) {
                    currentPosition--
                    setQuestion()
                }
            }
        }
    }

    private fun finishQuiz() {
        var score = 0
        for (i in questionsList.indices) {
            if (questionsList[i].correctAnswer == mAnswers.getOrNull(i)) {
                score++
            }
        }
        // Show the result (score) or navigate to a result screen
        // ...
    }


    private fun selectedOptionView(tv: TextView, selectedOptionNum: Int) {
        resetOptionsBackground() // Reset background of all options
        mSelectedOptionPosition = selectedOptionNum // Save the selected option position
//        tv.setBackgroundResource(R.drawable.selected_option_border) // Change background of the selected option
        tv.setTextColor(Color.parseColor("#363A43"))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(this, R.drawable.selected_option_border)
        // You can also change text color, style, etc. here if needed
    }

    private fun answerView(answer: Int, drawableView: Int) {
        when (answer) {
            1 -> {
                binding.tvOptionOne?.background = ContextCompat.getDrawable(this,drawableView)
            }
            2 -> {
                binding.tvOptionTwo?.background = ContextCompat.getDrawable(this,drawableView)
            }
            3 -> {
                binding.tvOptionThree?.background = ContextCompat.getDrawable(this,drawableView)
            }
            4 -> {
                binding.tvOptionFour?.background = ContextCompat.getDrawable(this,drawableView)
            }
        }
    }


}