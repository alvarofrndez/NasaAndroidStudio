package com.example.nasa.controllers

import android.util.Log
import androidx.compose.runtime.MutableState
import com.example.nasa.models.EONETService
import com.example.nasa.models.Event
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object eventsApi {
    suspend fun fetchEvents(): List<Event> {
        return try {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://eonet.gsfc.nasa.gov/api/v2.1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = retrofit.create(EONETService::class.java)
            val response = service.getEvents(limit = 10).execute()

            if (response.isSuccessful) {
                response.body()?.events ?: emptyList()
            } else {
                Log.e("Network", "Error: ${response.code()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("Network", "Exception: ${e.message}", e)
            emptyList()
        }
    }
}

fun convertDateFormat(originalDate: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
    val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.ENGLISH)

    val parsedDate = LocalDateTime.parse(originalDate, inputFormatter)
    return outputFormatter.format(parsedDate)
}