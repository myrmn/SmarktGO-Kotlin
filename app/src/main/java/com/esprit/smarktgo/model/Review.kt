package com.esprit.smarktgo.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class addReview(
    val title:String,
    val username:String,
    val userId:String,
    val supermarketName:String,
    val supermarketId:String,
    val description:String,
    val rating:Float,
)
data class Review(
    val title:String,
    val username:String,
    val userId:String,
    val supermarketName:String,
    val supermarketId:String,
    val description:String,
    val rating:Float,
    @SerializedName("createdAt") val date:String
): Serializable


data class SupermarketReview(
    val supermarketId: String
)
