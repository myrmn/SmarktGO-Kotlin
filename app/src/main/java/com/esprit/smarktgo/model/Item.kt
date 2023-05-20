package com.esprit.smarktgo.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Item(
    @SerializedName("_id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("image") val image:String,
    @SerializedName("description") val description: String,
    @SerializedName("price") val price: Double,
    @SerializedName("category") val category: String,
    @SerializedName("supermarketId") val supermarketId: String,
    @SerializedName("supermarketName") val supermarketName: String,
    @SerializedName("quantity") var quantity: Int
): Serializable


data class ItemInfo(
    @SerializedName("category") val category: String,
    @SerializedName("supermarketId") val supermarketId:String,
):Serializable