package com.esprit.smarktgo.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Order (
    @SerializedName("userId") val userId: String,
    @SerializedName("items") val items: ArrayList<Item>
): Serializable

data class GetOrder (
    @SerializedName("_id") val id: String,
    @SerializedName("group") val group: ArrayList<String>,
    @SerializedName("items") val items: ArrayList<Item>
): Serializable

data class AddUser (
    @SerializedName("userId") val userId: String,
    @SerializedName("group") val group: ArrayList<String>,
): Serializable