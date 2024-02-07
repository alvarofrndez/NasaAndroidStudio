package com.example.nasa.views

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.nasa.models.NasaApiService
import com.example.nasa.models.NasaApodResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun daily(navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .padding(bottom = 70.dp)
    ) {
        item {
            TopAppBar(
                title = {
                    Text(
                        text = "Imagen diaria",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )}
            )
            NasaPictureOfTheDay()
        }
    }
}

class NasaViewModelDaily : ViewModel() {
    val rxAdapter = RxJava3CallAdapterFactory.create()
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.nasa.gov/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(rxAdapter)
        .build()

    private val apiService = retrofit.create(NasaApiService::class.java)

    private val _nasaData = MutableStateFlow<NasaApodResponse?>(null)
    val nasaData: StateFlow<NasaApodResponse?> = _nasaData

    @SuppressLint("CheckResult")
    fun fetchAstronomyPictureOfTheDay(apiKey: String) {
        apiService.getAstronomyPictureOfTheDay(apiKey)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                _nasaData.value = result
            }, { error ->
                error.printStackTrace()
            })
    }
}

@Composable
fun NasaPictureOfTheDay(viewModel: NasaViewModelDaily = viewModel()) {
    val nasaData = viewModel.nasaData.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.fetchAstronomyPictureOfTheDay("tWAU957dk4Eqx7CM9fWeBCFtGflJhWju3RJB9xIn")
    }

    nasaData?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = nasaData.value?.title ?: "Título no disponible",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = rememberImagePainter(nasaData.value?.url),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(shape = RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = nasaData.value?.explanation ?: "Sin explicación disponible",
                fontSize = 16.sp,
                color = Color.Gray
            )
        }
    }
}