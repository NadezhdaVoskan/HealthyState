package com.example.healthystate.Models

data class ChangeFoodModel(
    val idFood: Int,
    val nameFood: String?,
    val grams: Int,
    val calories: Int,
    val descriptionFood: String,
    val dayFood: String?,
    val logPassId: Int,
    val eatingId: Int
)