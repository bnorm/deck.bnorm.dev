package dev.bnorm.deck

sealed class Video {
    data class Youtube(val id: String): Video()
    data class Vimeo(val id: String): Video()
}
