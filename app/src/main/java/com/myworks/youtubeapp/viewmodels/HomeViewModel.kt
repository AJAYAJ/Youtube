package com.myworks.youtubeapp.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.myworks.youtubeapp.models.VideoItemData
import com.myworks.youtubeapp.ui.theme.*
import com.myworks.youtubeapp.utils.VideoIdsProvider

class HomeViewModel : ViewModel() {
    private val _videoIds = mutableStateOf(VideoIdsProvider.getAllVideoIds())
    val videoIds: State<List<String>> = _videoIds

    private val _videoData = mutableStateOf(
        listOf(
            VideoItemData("New Gadget Review!", "0:15", "43K", AppGrey),
            VideoItemData("Travel Vlog #5", "8:15", "25K", AppGreyVariant),
            VideoItemData("Camera Review", "5:15", "12K", AppGreyVariant2),
            VideoItemData("Gaming Highlights", "10:30", "102K", AppDarkGrey),
            VideoItemData("Product Review", "3:45", "8K", AppMediumGrey),
            VideoItemData("Travel Vlog #6", "12:00", "55K", AppSlateGrey)
        )
    )
    val videoData: State<List<VideoItemData>> = _videoData
}