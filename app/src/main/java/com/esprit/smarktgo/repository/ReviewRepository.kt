package com.esprit.smarktgo.repository


import com.esprit.smarktgo.model.Review
import com.esprit.smarktgo.model.SupermarketReview
import com.esprit.smarktgo.model.addReview
import com.esprit.smarktgo.utils.ApiInterface
import com.esprit.smarktgo.utils.RetrofitInstance
import retrofit2.Response
import retrofit2.http.Body

class ReviewRepository {
    val api = RetrofitInstance.getRetroInstance().create(ApiInterface::class.java)

    suspend fun submitReview (review: addReview): Response<addReview>?
    {
        val request =api.submitReview(review)

        if (request.code()!=200) return null
        return request
    }
    suspend fun getSupermarketReviews(@Body supermarketId:String):MutableList<Review>?
    {
        val request =api.getSupermarketReviews(SupermarketReview(supermarketId = supermarketId))

        if (request.code()!=200) return null
        return request.body()
    }


}