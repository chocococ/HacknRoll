package com.example.quizmodoro

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Question (
    val id: Int,
    val question: String,
    // Resource ID for the image, if any
    val optionOne: String,
    val optionTwo: String,
    val optionThree: String,
    val optionFour: String,
    val correctAnswer: Int // Assuming this is the index of the correct answer
) : Parcelable