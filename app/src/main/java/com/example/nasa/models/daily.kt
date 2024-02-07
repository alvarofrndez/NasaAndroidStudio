package com.example.nasa.models

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

data class NasaApodResponse(
    val title: String,
    val explanation: String,
    val url: String,
    val date: String
)

interface NasaApiService {
    @GET("planetary/apod")
    fun getAstronomyPictureOfTheDay(
        @Query("api_key") apiKey: String,
        @Query("date") date: String? = null
    ): Single<NasaApodResponse>
}
