package com.myworks.youtubeapp.ui.screens

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.myworks.youtubeapp.DefaultCustomUiActivity
import com.myworks.youtubeapp.ui.components.ChannelGrid
import com.myworks.youtubeapp.ui.components.TopHeader
import com.myworks.youtubeapp.ui.components.VideoGrid
import com.myworks.youtubeapp.ui.theme.AppDarkBlue
import com.myworks.youtubeapp.ui.theme.AppOrange
import com.myworks.youtubeapp.viewmodels.HomeViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var selectedChannelName by remember { mutableStateOf<String?>(null) }
    var selectedCategoryName by remember { mutableStateOf<String?>(null) }
    
    val tabs = listOf("Latest Updated", "Categories", "Channel")
    val context = LocalContext.current
    val navBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    // Handle back button when a filter is active
    BackHandler(enabled = selectedChannelName != null || selectedCategoryName != null) {
        selectedChannelName = null
        selectedCategoryName = null
    }

    Scaffold(
        topBar = { TopHeader() },
        containerColor = Color.White
    ) { innerPadding ->
        Column(modifier = Modifier.padding(top = innerPadding.calculateTopPadding())) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = AppDarkBlue,
                contentColor = Color.White,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = AppOrange
                    )
                },
                divider = {}
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { 
                            selectedTabIndex = index 
                            selectedChannelName = null
                            selectedCategoryName = null
                        },
                        text = {
                            Text(
                                text = title,
                                fontSize = 14.sp,
                                color = if (selectedTabIndex == index) Color.White else Color.White.copy(alpha = 0.7f),
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            when (selectedTabIndex) {
                0 -> { // Latest Updated
                    VideoGrid(
                        videoIds = viewModel.videoIds.value,
                        videoData = viewModel.videoData.value,
                        contentPadding = navBarPadding,
                        onVideoClick = { id ->
                            val intent = Intent(context, DefaultCustomUiActivity::class.java).apply {
                                putExtra("VIDEO_ID", id)
                            }
                            context.startActivity(intent)
                        }
                    )
                }
                1 -> { // Categories
                    VideoGrid(
                        videoIds = viewModel.videoIds.value,
                        videoData = viewModel.videoData.value,
                        contentPadding = navBarPadding,
                        onVideoClick = { id ->
                            val intent = Intent(context, DefaultCustomUiActivity::class.java).apply {
                                putExtra("VIDEO_ID", id)
                            }
                            context.startActivity(intent)
                        }
                    )
                }
                2 -> { // Channel
                    if (selectedChannelName == null) {
                        ChannelGrid(
                            channelNames = viewModel.getChannelNames(),
                            contentPadding = navBarPadding,
                            onChannelClick = { name ->
                                selectedChannelName = name
                            }
                        )
                    } else {
                        val channelVideoIds = remember(selectedChannelName, viewModel.videoData.value) {
                            viewModel.getVideosByChannel(selectedChannelName!!)
                        }
                        val channelVideoData = remember(selectedChannelName, viewModel.videoData.value) {
                            viewModel.getVideoDataByChannel(selectedChannelName!!)
                        }
                        VideoGrid(
                            videoIds = channelVideoIds,
                            videoData = channelVideoData,
                            contentPadding = navBarPadding,
                            onVideoClick = { id ->
                                val intent = Intent(context, DefaultCustomUiActivity::class.java).apply {
                                    putExtra("VIDEO_ID", id)
                                }
                                context.startActivity(intent)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
