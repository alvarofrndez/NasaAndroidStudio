package com.example.nasa.controllers

import android.util.Log
import androidx.compose.runtime.Composable
import com.example.nasa.models.EPICService
import com.example.nasa.models.EpicResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object epicApi {
    suspend fun fetchEpicData(): List<EpicResponse> {
        return try {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://epic.gsfc.nasa.gov/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = retrofit.create(EPICService::class.java)
            val response = service.getEpicData()

            if (response.isSuccessful) {
                response.body() ?: emptyList()
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

@Composable
fun getImagen(url: String): String{
    var image = "https://epic.gsfc.nasa.gov/archive/natural";

    val parts = url.split("_")
    if (parts.size < 3) {
        return ""
    }

    val year = "/" + parts[2].substring(0, 4)
    val month = "/" + parts[2].substring(4, 6)
    val day = "/" + parts[2].substring(6, 8) + "/png/" + url + ".png"

    image += year + month + day
    return image
}