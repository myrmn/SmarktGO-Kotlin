package com.esprit.smarktgo.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Ticket (
    @SerializedName("code") val code: Int,
    @SerializedName("value") val value: Int,
    @SerializedName("used") val used:Boolean
): Serializable


data class UpdateTicket (
    @SerializedName("code") val code: Int,
    @SerializedName("userId") val userId: String,
    @SerializedName("wallet") val wallet:Double
): Serializable