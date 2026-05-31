package com.myworks.youtubeapp.utils

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.tasks.await

object RemoteConfigManager {
    private const val TAG = "RemoteConfigManager"
    const val CHANNELS_KEY = "AAA_Channels_list"
    
    private val remoteConfig = Firebase.remoteConfig

    init {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
    }

    suspend fun fetchAndActivate(): Boolean {
        return try {
            val result = remoteConfig.fetchAndActivate().await()
            Log.d(TAG, "Fetch and activate result: $result")
            true // Even if result is false (no updates), the fetch was successful
        } catch (e: Exception) {
            Log.e(TAG, "Fetch and activate failed", e)
            false
        }
    }

    fun getString(key: String): String {
        val value = remoteConfig.getString(key)
        Log.d(TAG, "Getting string for key: $key, value length: ${value.length}")
        return value
    }
}
