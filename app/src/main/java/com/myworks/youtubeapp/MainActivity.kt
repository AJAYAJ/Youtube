package com.myworks.youtubeapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.myworks.youtubeapp.ui.theme.YouTubeAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YouTubeAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    YouTubeScreen(
                        youtubeUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ", // Example URL
                        scrollableText = "This is a scrollable text area. ".repeat(100),
                        modifier = Modifier.padding(innerPadding)
                    )

                }
            }
        }
    }
}

@Composable
fun YouTubeScreen(youtubeUrl: String, scrollableText: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column(modifier = modifier.fillMaxSize()) {
        // Video Player Section (30% of the screen)

        // Scrollable Text Section (70% of the screen)
        Column(
            modifier = Modifier
                .weight(0.7f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Button(onClick = {
                val intent = Intent(context, YoutubeComposeActivity::class.java)
                context.startActivity(intent)
            }) {
                Text("Go to YoutubeComposeActivity")
            }

            Button(onClick = {
                val intent = Intent(context, DefaultCustomUiActivity::class.java)
                context.startActivity(intent)
            }) {
                Text("Go to DefaultCustomUiActivity")
            }

            Text(
                text = scrollableText
            )
        }
    }
}

