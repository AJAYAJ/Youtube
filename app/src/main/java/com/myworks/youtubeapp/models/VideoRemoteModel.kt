package com.myworks.youtubeapp.models

import com.google.gson.annotations.SerializedName

data class VideoRemoteModel(
    @SerializedName("videoId") val videoId: String,
    @SerializedName("title") val title: String,
    @SerializedName("keywords") val keywords: String,
    @SerializedName("timestamp") val timestamp: Long,
    @SerializedName("category") val category: String,
    @SerializedName("channelName") val channelName: String,
    @SerializedName("story") val story: String
)
