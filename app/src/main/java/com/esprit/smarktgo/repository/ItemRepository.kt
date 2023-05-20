package com.esprit.smarktgo.repository

import com.esprit.smarktgo.model.Item
import com.esprit.smarktgo.model.ItemInfo
import com.esprit.smarktgo.utils.ApiInterface
import com.esprit.smarktgo.utils.RetrofitInstance

class ItemRepository {

    val api = RetrofitInstance.getRetroInstance().create(ApiInterface::class.java)

    suspend fun getAllBySupermarketIdAndCategory(category: String, supermarketId: String): MutableList<Item>?
    {
        val request =api.getAllBySupermarketIdAndCategory(ItemInfo(category=category,supermarketId = supermarketId))

        if (request.code()!=200) return null
        return request.body()
    }

}