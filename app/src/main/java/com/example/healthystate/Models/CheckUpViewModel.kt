package com.example.healthystate.Models

data class CheckUpViewModel (
    var doctorTestId: Int,
    var logPassId:Int,
    var statusDoctorTest:Boolean,
    val dateCheck: String
        )