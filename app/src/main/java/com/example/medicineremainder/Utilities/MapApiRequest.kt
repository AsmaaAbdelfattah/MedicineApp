package com.example.medicineremainder.Utilities

import com.example.medicineremainder.Model.MapResponse

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call


interface MapApiRequest {
        @GET("search/2/nearbySearch/.json")
     fun getNearbyPlaces(
            @Query("key") apiKey: String,
            @Query("lat") lat: Double,
            @Query("lon") lon: Double,
            @Query("radius") radius: Int,
            @Query("categorySet") categorySet: String
        ): Call<MapResponse>

}