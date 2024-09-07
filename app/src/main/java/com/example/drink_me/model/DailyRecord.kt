package com.example.drink_me.model

data class DailyRecord(
    val date: String,
    val weight: Int,
    val waterIntake: Int,
    val goalReached: Boolean
)
