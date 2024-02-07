package com.example.nasa.models

import retrofit2.Response
import retrofit2.http.GET

data class EpicResponse(
    val identifier: String,
    val caption: String,
    val image: String,
    val version: String,
    val centroid_coordinates: Coordinates?,
    val dscovr_j2000_position: Position?,
    val lunar_j2000_position: Position?,
    val sun_j2000_position: Position?,
    val attitude_quaternions: AttitudeQuaternions?,
    val date: String,
    val coords: Coordinates?
)

data class Coordinates(
    val lat: Double,
    val lon: Double
)

data class Position(
    val x: Double,
    val y: Double,
    val z: Double
)

data class AttitudeQuaternions(
    val q0: Double,
    val q1: Double,
    val q2: Double,
    val q3: Double
)

interface EPICService {
    @GET("natural")
    suspend fun getEpicData(): Response<List<EpicResponse>>
}