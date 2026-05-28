package com.myworks.youtubeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.myworks.youtubeapp.ui.screens.HomeScreen
import com.myworks.youtubeapp.ui.theme.YouTubeAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YouTubeAppTheme {
                HomeScreen()
            }
        }
    }
}
