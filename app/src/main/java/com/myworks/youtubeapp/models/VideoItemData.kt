package com.myworks.youtubeapp.models

import androidx.compose.ui.graphics.Color

data class VideoItemData(
    val videoId: String,
    val title: String,
    val keywords: String,
    val timestamp: Long,
    val views: String,
    val color: Color,
    val story: String,
    val channelName: String
)
