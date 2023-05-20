package com.esprit.smarktgo.repository

import android.util.Log
import com.esprit.smarktgo.model.AddUser
import com.esprit.smarktgo.model.GetOrder
import com.esprit.smarktgo.model.Order
import com.esprit.smarktgo.model.User
import com.esprit.smarktgo.utils.ApiInterface
import com.esprit.smarktgo.utils.RetrofitInstance
import retrofit2.Response

class OrderRepository {

    val api = RetrofitInstance.getRetroInstance().create(ApiInterface::class.java)

    suspend fun addToCart(order: Order): Response<GetOrder>?
    {
        val request =api.addToCart(order)

        if (request.code()!=200 && request.code()!=201 && request.code()!=400) return null
        return request
    }

    suspend fun get(userId: String): GetOrder?
    {
        val request =api.getOrder(User(userId,"",0.0))

        if (request.code()!=200) return null
        return request.body()
    }

    suspend fun removeItem(order: GetOrder): Response<GetOrder>?
    {
        val request =api.removeItem(order)

        if (request.code()!=200) return null
        return request
    }

    suspend fun deleteOrder(user: User): Response<GetOrder>?
    {
        val request =api.deleteOrder(user)

        if (request!!.code()!=200) return null
        return request
    }

    suspend fun addUser(order: AddUser): Response<AddUser>?
    {
        val request =api.addUser(order)

        if (request!!.code()!=200) return null
        return request
    }
}