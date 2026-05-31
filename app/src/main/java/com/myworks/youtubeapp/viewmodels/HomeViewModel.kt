package com.myworks.youtubeapp.viewmodels

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.myworks.youtubeapp.data.VideoRepository
import com.myworks.youtubeapp.data.local.AppDatabase
import com.myworks.youtubeapp.models.VideoItemData
import com.myworks.youtubeapp.ui.theme.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: VideoRepository
    
    private val _videoIds = mutableStateOf<List<String>>(emptyList())
    val videoIds: State<List<String>> = _videoIds

    private val _videoData = mutableStateOf<List<VideoItemData>>(emptyList())
    val videoData: State<List<VideoItemData>> = _videoData
    
    private val _channelNames = mutableStateOf<List<String>>(emptyList())
    val channelNames: State<List<String>> = _channelNames

    private val colors = listOf(AppGrey, AppGreyVariant, AppGreyVariant2, AppDarkGrey, AppMediumGrey, AppSlateGrey)

    init {
        val db = AppDatabase.getDatabase(application)
        repository = VideoRepository(db.appDao())
        
        observeData()
        
        viewModelScope.launch {
            repository.refreshData()
        }
    }

    private fun observeData() {
        viewModelScope.launch {
            repository.allChannels.collectLatest { channels ->
                _channelNames.value = channels.map { it.name }
            }
        }

        viewModelScope.launch {
            repository.allVideos.collectLatest { videos ->
                _videoIds.value = videos.map { it.videoId }
                _videoData.value = videos.mapIndexed { index, video ->
                    VideoItemData(
                        videoId = video.videoId,
                        title = video.title,
                        keywords = video.keywords,
                        timestamp = video.timestamp,
                        views = "0K",
                        color = colors[index % colors.size],
                        story = video.story,
                        channelName = video.channelName
                    )
                }
            }
        }
    }
    
    fun getVideosByChannel(channelName: String): List<String> {
        return _videoData.value
            .filter { it.channelName == channelName }
            .map { it.videoId }
    }

    fun getVideoDataByChannel(channelName: String): List<VideoItemData> {
        return _videoData.value.filter { it.channelName == channelName }
    }
    
    fun getChannelNames(): List<String> = _channelNames.value
    
    fun refresh() {
        viewModelScope.launch {
            repository.refreshData()
        }
    }
}
