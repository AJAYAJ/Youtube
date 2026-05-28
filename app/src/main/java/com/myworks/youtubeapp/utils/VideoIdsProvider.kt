package com.myworks.youtubeapp.utils

import kotlin.random.Random

object VideoIdsProvider {
    private val videoIds = arrayOf("gyhl3UHFWHE", "gd141sa3-mM", "MWa5O2CtnOs", "cw2hI63q_J4", "USH0KxqWso4")
    private val liveVideoIds = arrayOf("hHW1oY26kxQ")
    private val random = Random.Default

    fun getNextVideoId(): String {
        return videoIds[random.nextInt(videoIds.size)]
    }

    fun getAllVideoIds(): List<String> {
        return videoIds.toList()
    }

    fun getVideosByCategory(categoryId: Int): List<String> {
        // Return different videos based on ID to demonstrate "coming based on id"
        return when (categoryId) {
            1 -> videoIds.toList()
            2 -> listOf(videoIds[0], videoIds[1]) // Music
            3 -> listOf(videoIds[2], videoIds[3]) // Gaming
            4 -> listOf(videoIds[4])               // Movies
            5 -> listOf(videoIds[0], videoIds[4]) // Sports
            6 -> listOf(videoIds[1], videoIds[2]) // News
            else -> videoIds.toList()
        }
    }
}
