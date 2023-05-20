package com.esprit.smarktgo.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
    @SerializedName("id") val id: String,
    @SerializedName ("fullName") val fullName:String,
    @SerializedName ("wallet") val wallet:Double,
    ): Serializable
data class updateUsername(
    @SerializedName("id") val id: String,
    @SerializedName ("fullName") val fullName:String,
): Serializable

data class ProfileItem (
    val item:String,
    val image:Int
)