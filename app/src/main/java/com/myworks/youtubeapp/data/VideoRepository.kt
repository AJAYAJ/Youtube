package com.myworks.youtubeapp.data

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.myworks.youtubeapp.data.local.AppDao
import com.myworks.youtubeapp.data.local.ChannelEntity
import com.myworks.youtubeapp.data.local.VideoEntity
import com.myworks.youtubeapp.models.ChannelListRemoteModel
import com.myworks.youtubeapp.models.VideoRemoteModel
import com.myworks.youtubeapp.utils.RemoteConfigManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class VideoRepository(private val appDao: AppDao) {

    val allChannels: Flow<List<ChannelEntity>> = appDao.getAllChannels()
    val allVideos: Flow<List<VideoEntity>> = appDao.getAllVideos()

    suspend fun refreshData() = withContext(Dispatchers.IO) {
        Log.d("VideoRepository", "Starting data refresh...")
        
        try {
            RemoteConfigManager.fetchAndActivate()
        } catch (e: Exception) {
            Log.e("VideoRepository", "Remote Config fetch threw an exception", e)
            // We can still try to read cached values
        }

        // 1. Sync Channels
        val channelsJson = RemoteConfigManager.getString(RemoteConfigManager.CHANNELS_KEY)
        if (channelsJson.isEmpty()) {
            Log.w("VideoRepository", "Channels JSON is empty")
            return@withContext
        }

        val channelListModel = try {
            Gson().fromJson(channelsJson, ChannelListRemoteModel::class.java)
        } catch (e: Exception) {
            Log.e("VideoRepository", "Error parsing channels JSON", e)
            null
        }

        val channelEntities = channelListModel?.channels?.map { name ->
            ChannelEntity(name, name.replace(" ", "_"))
        } ?: emptyList()

        if (channelEntities.isNotEmpty()) {
            appDao.insertChannels(channelEntities)
            Log.d("VideoRepository", "Inserted ${channelEntities.size} channels")
        }

        // 2. Sync Videos for each channel
        channelEntities.forEach { channel ->
            val videosJson = RemoteConfigManager.getString(channel.remoteKey)
            if (videosJson.isNotEmpty()) {
                try {
                    val type = object : TypeToken<List<VideoRemoteModel>>() {}.type
                    val remoteVideos: List<VideoRemoteModel> = Gson().fromJson(videosJson, type)
                    
                    val videoEntities = remoteVideos.map { it.toEntity(channel.name) }
                    appDao.insertVideos(videoEntities)
                    Log.d("VideoRepository", "Inserted ${videoEntities.size} videos for channel: ${channel.name}")
                } catch (e: Exception) {
                    Log.e("VideoRepository", "Error parsing videos for ${channel.name}", e)
                }
            } else {
                Log.w("VideoRepository", "No video data found for key: ${channel.remoteKey}")
            }
        }
    }

    private fun VideoRemoteModel.toEntity(channelName: String): VideoEntity {
        return VideoEntity(
            videoId = this.videoId,
            title = this.title,
            keywords = this.keywords,
            timestamp = this.timestamp,
            category = this.category,
            channelName = channelName,
            story = this.story
        )
    }
}
