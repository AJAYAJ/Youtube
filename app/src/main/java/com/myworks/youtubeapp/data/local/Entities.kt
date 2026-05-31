package com.myworks.youtubeapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "channels")
data class ChannelEntity(
    @PrimaryKey val name: String,
    val remoteKey: String // Name with underscores
)

@Entity(tableName = "videos")
data class VideoEntity(
    @PrimaryKey val videoId: String,
    val title: String,
    val keywords: String,
    val timestamp: Long,
    val category: String,
    val channelName: String,
    val story: String
)
