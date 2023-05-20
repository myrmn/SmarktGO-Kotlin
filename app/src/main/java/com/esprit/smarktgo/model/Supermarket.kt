package com.esprit.smarktgo.model
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Supermarket (
    @SerializedName("_id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("image") val image:String,
    @SerializedName("description") val description: String,
    @SerializedName("address") val address: String,
    @SerializedName("location") val location: Location,
    @SerializedName ("favorites") val favorites:ArrayList<String>
): Serializable


data class Location(
    @SerializedName("type") val type: String,
    @SerializedName("coordinates") val coordinates: List<Double>
    ):Serializable

data class IsFavoriteBody(
    @SerializedName("supermarketId") val supermarketId: String,
    @SerializedName("userId") val userId: String,
):Serializable

data class AddRemoveFavorite(
    @SerializedName("supermarketId") val supermarketId: String,
    @SerializedName("favorites") val favorites: ArrayList<String>,
):Serializable