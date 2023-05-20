package com.esprit.smarktgo.repository


import com.esprit.smarktgo.model.AddRemoveFavorite
import com.esprit.smarktgo.model.IsFavoriteBody
import com.esprit.smarktgo.model.Supermarket
import com.esprit.smarktgo.model.User
import com.esprit.smarktgo.utils.ApiInterface
import com.esprit.smarktgo.utils.RetrofitInstance


class SupermarketRepository {

    val api = RetrofitInstance.getRetroInstance().create(ApiInterface::class.java)

    suspend fun getAll(): MutableList<Supermarket>?
    {
        val request =api.getAll()

        if (request.code()!=200) return null
        return request.body()
    }

    suspend fun getCategories(): MutableList<String>?
    {
        val request =api.getCategories()

        if (request.code()!=200) return null
        return request.body()
    }

    suspend fun getFavorites(user: User): MutableList<Supermarket>?
    {
        val request =api.getFavorites(user)

        if (request.code()!=200) return null
        return request.body()
    }

    suspend fun isFavorite(isFavoriteBody: IsFavoriteBody): MutableList<String>?
    {
        val request =api.isFavorite(isFavoriteBody)

        if (request.code()!=200) return null
        return request.body()
    }

    suspend fun addRemoveFavorite(addRemoveFavorite: AddRemoveFavorite): AddRemoveFavorite?
    {
        val request =api.addRemoveFavorite(addRemoveFavorite)

        if (request.code()!=200) return null
        return request.body()
    }

    suspend fun getNearest(coordinates:ArrayList<Int>): MutableList<Supermarket>?
    {
        val request =api.getNearest(coordinates)

        if (request.code()!=200) return null
        return request.body()
    }
}