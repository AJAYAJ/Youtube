package com.myworks.youtubeapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.myworks.youtubeapp.menu.MenuItem
import com.myworks.youtubeapp.utils.VideoIdsProvider
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class DefaultCustomUiActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DefaultCustomUiContent()
        }
    }
}

@Composable
fun DefaultCustomUiContent() {
    var videoId by remember { mutableStateOf(VideoIdsProvider.getNextVideoId()) }
    
    Column {
        YouTubePlayerWithDefaultCustomUi(
            videoId = videoId,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )

        Row {
            Button(onClick = { 
                videoId = VideoIdsProvider.getNextVideoId() 
            }) {
                Text("Play previous video")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = { 
                videoId = VideoIdsProvider.getNextVideoId() 
            }) {
                Text("Play next video")
            }
        }
    }
}

@Composable
private fun YouTubePlayerWithDefaultCustomUi(
    videoId: String,
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var player by remember { mutableStateOf<YouTubePlayer?>(null) }
    var currentVideoId by remember { mutableStateOf("") }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            YouTubePlayerView(context).apply {
                enableAutomaticInitialization = false
                lifecycleOwner.lifecycle.addObserver(this)

                val options = IFramePlayerOptions.Builder(context)
                    .controls(0) // disable default web UI
                    .build()
                
                initialize(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        player = youTubePlayer
                        
                        // Set the default custom UI controller
                        val defaultPlayerUiController = DefaultPlayerUiController(this@apply, youTubePlayer)
                        defaultPlayerUiController.showYouTubeButton(false)
                        defaultPlayerUiController.showMenuButton(true)
                        defaultPlayerUiController.showCustomAction1(true)
                        defaultPlayerUiController.showCustomAction2(true)
                        
                        // Example: adding a menu item
                        defaultPlayerUiController.getMenu()?.addItem(
                            MenuItem("Example Menu Item", R.drawable.ayp_ic_youtube_24dp) {
                                Toast.makeText(context, "Menu item clicked", Toast.LENGTH_SHORT).show()
                            }
                        )

                        setCustomPlayerUi(defaultPlayerUiController.rootView)
                        
                        if (currentVideoId != videoId) {
                            currentVideoId = videoId
                            youTubePlayer.loadOrCueVideo(lifecycleOwner.lifecycle, videoId, 0f)
                        }
                    }
                }, options)
            }
        },
        update = { view ->
            if (player != null && currentVideoId != videoId) {
                currentVideoId = videoId
                player?.loadOrCueVideo(lifecycleOwner.lifecycle, videoId, 0f)
            }
        }
    )
}
