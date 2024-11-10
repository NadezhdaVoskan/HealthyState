package com.example.healthystate.Models

data class ChangeDiaryModel(
    val idPersonalDiary: Int,
    val nameNotes: String,
    val notes: String,
    val dayPersonalDiary: String,
    val moodId: Int,
    val logPassId: Int
)