package com.myworks.youtubeapp.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.myworks.youtubeapp.DefaultCustomUiActivity
import com.myworks.youtubeapp.ui.components.TopHeader
import com.myworks.youtubeapp.ui.components.VideoGrid
import com.myworks.youtubeapp.ui.theme.AppDarkBlue
import com.myworks.youtubeapp.ui.theme.AppOrange
import com.myworks.youtubeapp.viewmodels.HomeViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Latest Updated", "Categories", "Channel")
    val context = LocalContext.current

    Scaffold(
        topBar = { TopHeader() },
        containerColor = Color.White
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
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
                        onClick = { selectedTabIndex = index },
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

            VideoGrid(
                videoIds = viewModel.videoIds.value,
                videoData = viewModel.videoData.value,
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

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}