package com.esprit.smarktgo.utils

import com.esprit.smarktgo.model.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface ApiInterface {

    @POST("user/signUp")
    suspend fun signUp(@Body user: User): Response<User>

    @POST("user/signIn")
    suspend fun signIn(@Body user: User):Response<User>

    @POST("user/update")
    suspend fun updateProfile(@Body user: User):Response<User>

    @POST("user/update")
    suspend fun updateUsername(@Body user: updateUsername): Response<updateUsername>

    @POST("supermarket/")
    suspend fun getNearest(@Body coordinates:ArrayList<Int>):Response<MutableList<Supermarket>>

    @GET("supermarket/")
    suspend fun getAll():Response<MutableList<Supermarket>>

    @GET("supermarket/getCategories")
    suspend fun getCategories():Response<MutableList<String>>

    @POST("supermarket/getFavorites")
    suspend fun getFavorites(@Body user: User):Response<MutableList<Supermarket>>

    @POST("supermarket/isFavorite")
    suspend fun isFavorite(@Body isFavoriteBody: IsFavoriteBody):Response<MutableList<String>>

    @POST("supermarket/addRemoveFavorite")
    suspend fun addRemoveFavorite(@Body addRemoveFavorite: AddRemoveFavorite):Response<AddRemoveFavorite>

    @POST("item/")
    suspend fun getAllBySupermarketIdAndCategory(@Body itemInfo: ItemInfo):Response<MutableList<Item>>

    @POST("order/add")
    suspend fun addToCart(@Body order: Order): Response<GetOrder>

    @POST("order/get")
    suspend fun getOrder(@Body user: User): Response<GetOrder>

    @POST("order/removeItem")
    suspend fun removeItem(@Body getOrder: GetOrder): Response<GetOrder>

    @POST("/order/delete")
    suspend fun deleteOrder(@Body user: User): Response<GetOrder>?


    @POST("user/signIn")
    fun getInfo(@Body user: User): Call<User>

    @GET("ticket/")
    suspend fun getAllTickets():Response<MutableList<Ticket>>

    @POST("ticket/update")
    suspend fun updateTicket(@Body updateTicket: UpdateTicket):Response<Ticket>

    @POST("user/getGroupMembers")
    suspend fun getGroupMembers(@Body order:GetOrder):Response<MutableList<User>>

    @GET("user/getAllUsers")
    suspend fun getAllUsers():Response<MutableList<User>>

    @POST("order/addUser")
    suspend fun addUser(@Body order:AddUser):Response<AddUser>

    @POST("review/create")
    suspend fun submitReview(@Body review:addReview):Response<addReview>

    @POST("review/")
    suspend fun getSupermarketReviews(@Body supermarketReview: SupermarketReview):Response<MutableList<Review>>


}


