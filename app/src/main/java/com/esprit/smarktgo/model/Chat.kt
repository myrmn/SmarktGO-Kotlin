package com.esprit.smarktgo.model

data class Chat(
    val dateTime: String,
    val userId: String,
    val message: String,
    val userName: String,
    val orderId: String,
    val order: Int,
)