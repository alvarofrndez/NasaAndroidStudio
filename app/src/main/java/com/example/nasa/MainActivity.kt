package com.example.nasa

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.nasa.ui.theme.NasaTheme
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.nasa.views.daily
import com.example.nasa.views.events
import com.example.nasa.views.explorer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NasaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App()
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val nav_controller = rememberNavController()
    var current_route = remember { mutableStateOf("daily")}

    Scaffold(
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                .height(70.dp)
            ){
                Spacer(modifier = Modifier.weight(1f))

                BottomBarItem(
                    icon = Icons.Default.Star,
                    label = "Diaria",
                    route = "daily",
                    current_route = current_route.value,
                    onClick = {
                        current_route.value = "daily"
                        nav_controller.navigate("daily")
                    }
                )

                Spacer(modifier = Modifier.weight(1f))

                BottomBarItem(
                    icon = Icons.Default.Favorite,
                    label = "Eventos",
                    route = "events",
                    current_route = current_route.value,
                    onClick = {
                        current_route.value = "events"
                        nav_controller.navigate("events")
                    }
                )

                Spacer(modifier = Modifier.weight(1f))

                BottomBarItem(
                    icon = Icons.Default.Search,
                    label = "Explorar",
                    route = "explorer",
                    current_route = current_route.value,
                    onClick = {
                        current_route.value = "explorer"
                        nav_controller.navigate("explorer")
                    }
                )

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    ){
        NavHost(
            navController = nav_controller,
            startDestination = current_route.value
        ) {
            composable("daily") {
                daily(nav_controller)
            }
            composable("events") {
                events(nav_controller)
            }
            composable("explorer"){
                explorer(nav_controller)
            }
        }
    }
}

@Composable
fun BottomBarItem(
    icon: ImageVector,
    label: String,
    route: String,
    current_route: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        IconButton(
            onClick = onClick,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (current_route == route)  Color.Blue else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = label,
            color = if (current_route == route) Color.Blue else Color.Gray,
            fontSize = 12.sp
        )
    }
}