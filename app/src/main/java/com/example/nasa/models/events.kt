package com.example.nasa.models

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

data class Event(
    val id: String,
    val title: String,
    val description: String,
    val geometries: List<Geometry>,
    @SerializedName("link") val detailsLink: String?
)

data class Geometry(
    val date: String,
    val type: String,
    val coordinates: List<Double>
)

data class EventsResponse(
    val events: List<Event>
)

interface EONETService {
    @GET("events")
    fun getEvents(@Query("limit") limit: Int): Call<EventsResponse>
}