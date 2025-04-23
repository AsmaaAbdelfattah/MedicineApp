package com.example.medicineremainder.Utilities

import com.example.medicineremainder.Model.Constants
import com.example.medicineremainder.Model.GeoapifyResponse

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call


interface GeoapifyService {
    @GET("places")
    fun getNearbyPharmacies(
        @Query("categories") categories: String = "healthcare.pharmacy",
        @Query("filter") filter: String,
        @Query("limit") limit: Int = 10,
        @Query("apiKey") apiKey: String = Constants.GEOAPIFY_API_KEY
    ): Call<GeoapifyResponse>
}