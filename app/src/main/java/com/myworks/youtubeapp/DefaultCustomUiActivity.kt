package com.myworks.youtubeapp

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
        val videoId = intent.getStringExtra("VIDEO_ID") ?: VideoIdsProvider.getNextVideoId()
        setContent {
            DefaultCustomUiContent(initialVideoId = videoId)
        }
    }
}

@Composable
fun DefaultCustomUiContent(initialVideoId: String) {
    var isFullscreen by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val activity = context as? ComponentActivity

    LaunchedEffect(isFullscreen) {
        if (isFullscreen) {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        YouTubePlayerWithDefaultCustomUi(
            videoId = initialVideoId,
            isFullscreen = isFullscreen,
            modifier = if (isFullscreen) {
                Modifier.fillMaxSize()
            } else {
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f)
            },
            onToggleFullscreen = { isFullscreen = !isFullscreen }
        )

        if (!isFullscreen) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Text(
                    text = "Video Details",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "This is a dummy scrollable text area. It provides more information about the video. ".repeat(50),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun YouTubePlayerWithDefaultCustomUi(
    videoId: String,
    isFullscreen: Boolean,
    modifier: Modifier = Modifier,
    onToggleFullscreen: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var player by remember { mutableStateOf<YouTubePlayer?>(null) }
    var currentVideoId by remember { mutableStateOf("") }
    var uiController by remember { mutableStateOf<DefaultPlayerUiController?>(null) }

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
                        val controller = DefaultPlayerUiController(this@apply, youTubePlayer)
                        uiController = controller
                        
                        controller.showYouTubeButton(false)
                        controller.showMenuButton(true)
                        controller.showCustomAction1(true)
                        controller.showCustomAction2(true)
                        
                        // Set custom fullscreen listener to notify Compose
                        controller.setFullscreenButtonClickListener {
                            onToggleFullscreen()
                        }
                        
                        // Example: adding a menu item
                        controller.getMenu().addItem(
                            MenuItem("Example Menu Item", R.drawable.ayp_ic_youtube_24dp) {
                                Toast.makeText(context, "Menu item clicked", Toast.LENGTH_SHORT).show()
                            }
                        )

                        setCustomPlayerUi(controller.rootView)
                        
                        if (currentVideoId != videoId) {
                            currentVideoId = videoId
                            youTubePlayer.loadOrCueVideo(lifecycleOwner.lifecycle, videoId, 0f)
                        }
                    }
                }, options)
            }
        },
        update = { view ->
            uiController?.setFullscreen(isFullscreen)
            if (player != null && currentVideoId != videoId) {
                currentVideoId = videoId
                player?.loadOrCueVideo(lifecycleOwner.lifecycle, videoId, 0f)
            }
        }
    )
}
