package dev.bnorm.dcnyc25.broadcast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class VoteMessage {
    abstract val userId: String

    @Serializable
    @SerialName("ping")
    class Ping(override val userId: String) : VoteMessage()

    @Serializable
    @SerialName("code")
    data class Code(
        override val userId: String,
        val value: Boolean? = null,
    ) : VoteMessage()

    @Serializable
    sealed class MultiVoteMessage : VoteMessage() {
        abstract val editor: Boolean?
        abstract val review: Boolean?
        abstract val slides: Boolean?
    }

    @Serializable
    @SerialName("context")
    data class Context(
        override val userId: String,
        override val editor: Boolean? = null,
        override val review: Boolean? = null,
        override val slides: Boolean? = null,
    ) : MultiVoteMessage()

    @Serializable
    @SerialName("style")
    data class Style(
        override val userId: String,
        override val editor: Boolean? = null,
        override val review: Boolean? = null,
        override val slides: Boolean? = null,
    ) : MultiVoteMessage()

    @Serializable
    @SerialName("Time")
    data class Time(
        override val userId: String,
        override val editor: Boolean? = null,
        override val review: Boolean? = null,
        override val slides: Boolean? = null,
    ) : MultiVoteMessage()
}
