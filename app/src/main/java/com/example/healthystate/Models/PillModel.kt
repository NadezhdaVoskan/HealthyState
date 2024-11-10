package com.example.healthystate.Models

data class PillModel (
    val pillName: String?,
    val countPill: Int,
    val time: String,
    val dayPill: String,
    val logPassId: Int,
    val dayPillEnd: String
    )