package com.example.healthystate.Models

data class ChangeCheckUpModel(
    val idDoctorTestView: Int,
    val doctorTestId: Int,
    val logPassId: Int,
    val statusDoctorTest: Boolean,
    var dataCheck: String
)
