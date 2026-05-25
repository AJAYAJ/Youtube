package com.myworks.youtubeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
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

        Button(onClick = { 
            videoId = VideoIdsProvider.getNextVideoId() 
        }) {
            Text("Play next video")
        }
    }
}

@Composable
private fun YouTubePlayerWithDefaultCustomUi(
    videoId: String,
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val playerRef = remember { mutableStateOf<YouTubePlayer?>(null) }

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
                        playerRef.value = youTubePlayer
                        
                        // Set the default custom UI controller
                        val defaultPlayerUiController = DefaultPlayerUiController(this@apply, youTubePlayer)
                        defaultPlayerUiController.showYouTubeButton(false)
                        setCustomPlayerUi(defaultPlayerUiController.rootView)
                        
                        youTubePlayer.loadOrCueVideo(lifecycleOwner.lifecycle, videoId, 0f)
                    }
                }, options)
            }
        },
        update = { view ->
            playerRef.value?.loadOrCueVideo(lifecycleOwner.lifecycle, videoId, 0f)
        }
    )
}
