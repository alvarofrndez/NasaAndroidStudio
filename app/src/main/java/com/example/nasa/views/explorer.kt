package com.example.nasa.views

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.nasa.controllers.epicApi
import com.example.nasa.controllers.getImagen
import com.example.nasa.models.EPICService
import com.example.nasa.models.EpicResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun explorer(navController: NavController) {
    TopAppBar(
        title = {
            Text(
                text = "Explorador",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )}
    )
    EpicImage()
}

@Composable
fun EpicImage() {
    var epicData = remember { mutableStateOf<List<EpicResponse>?>(emptyList()) }

    LaunchedEffect(Unit) {
        try {
            val epicResponseList = withContext(Dispatchers.IO) {
                epicApi.fetchEpicData()
            }
            epicData.value = epicResponseList
        } catch (e: Exception) {
            Log.e("Network", "Exception: ${e.message}", e)
        }
    }

    LazyColumn(
        modifier = Modifier.padding(bottom = 70.dp, top = 70.dp)
    ) {
        items(epicData.value ?: emptyList()) { response ->
            response?.let {
                var showDialog = remember { mutableStateOf(false) }
                if (showDialog.value) {
                    AlertDialog(
                        onDismissRequest = { showDialog.value = false },
                        title = { Text(text = "Detalles") },
                        confirmButton = {
                            Button(
                                onClick = { showDialog.value = false },
                                colors = ButtonDefaults.buttonColors(Color.Gray),
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Text("Cerrar")
                            }
                        },
                        text = {
                            modalEpicData(response)
                        }
                    )
                }
                cardEpicData(response, showDialog)
            }
        }
    }
}

@Composable
fun modalEpicData(response: EpicResponse){
    val imageUrl = getImagen(response.image)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(MaterialTheme.shapes.medium)
        ) {
            Image(
                painter = rememberImagePainter(imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "Descripción: ",
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = "${response.caption}",
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "Fecha: ",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "${response.date}",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        response.centroid_coordinates?.let { coordinates ->
            Column (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ){
                Text(
                    text = "Coordenadas centrales:",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Latitud: ${coordinates.lat}, Longitud: ${coordinates.lon}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        response.dscovr_j2000_position?.let { dscovrPosition ->
            Column (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ){
                Text(
                    text = "Posición DSCOVR J2000:",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "X: ${dscovrPosition.x}, Y: ${dscovrPosition.y}, Z: ${dscovrPosition.z}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun cardEpicData(response: EpicResponse, modal: MutableState<Boolean>){
    val imageUrl = getImagen(response.image)
    Log.d("imagen", imageUrl)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                modal.value = true
            },
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(MaterialTheme.shapes.medium)
        ) {
            Image(
                painter = rememberImagePainter(imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "Descripción: ",
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = "${response.caption}",
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "Fecha: ",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "${response.date}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}