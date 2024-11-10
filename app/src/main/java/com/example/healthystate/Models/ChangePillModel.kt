package com.example.healthystate.Models

data class ChangePillModel(
    val idPill: Int,
val pillName: String?,
val countPill: Int,
val time: String,
val dayPill: String,
val dayPillEnd: String,
val logPassId: Int)