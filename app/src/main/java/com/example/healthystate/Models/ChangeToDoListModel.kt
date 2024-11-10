package com.example.healthystate.Models

data class ChangeToDoListModel (
    val idToDoList: Int,
    val task: String?,
    val logPassId: Int,
    val statusList: Boolean
        )