package com.myworks.youtubeapp.models

import com.google.gson.annotations.SerializedName

data class ChannelListRemoteModel(
    @SerializedName("channels") val channels: List<String>
)
