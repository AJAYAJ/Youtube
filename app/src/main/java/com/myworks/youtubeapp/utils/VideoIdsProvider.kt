package com.myworks.youtubeapp.utils

import kotlin.random.Random

object VideoIdsProvider {
    private val videoIds = arrayOf("gyhl3UHFWHE", "gd141sa3-mM", "MWa5O2CtnOs", "cw2hI63q_J4", "USH0KxqWso4")
    private val liveVideoIds = arrayOf("hHW1oY26kxQ")
    private val random = Random.Default

    fun getNextVideoId(): String {
        return videoIds[random.nextInt(videoIds.size)]
    }

    fun getNextLiveVideoId(): String {
        return liveVideoIds[random.nextInt(liveVideoIds.size)]
    }
}
