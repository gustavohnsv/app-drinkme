package com.gustavohnsv.drink_me.model

data class DailyRecord(
    val date: String,
    val weight: Int,
    val waterIntake: Int,
    val goalReached: Boolean
)