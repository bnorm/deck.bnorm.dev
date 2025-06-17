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

        abstract fun copy(
            editor: Boolean? = this.editor,
            review: Boolean? = this.review,
            slides: Boolean? = this.slides,
        ): MultiVoteMessage
    }

    @Serializable
    @SerialName("context")
    data class Context(
        override val userId: String,
        override val editor: Boolean? = null,
        override val review: Boolean? = null,
        override val slides: Boolean? = null,
    ) : MultiVoteMessage() {
        override fun copy(editor: Boolean?, review: Boolean?, slides: Boolean?): Context {
            return copy(userId = userId, editor = editor, review = review, slides = slides)
        }
    }

    @Serializable
    @SerialName("style")
    data class Style(
        override val userId: String,
        override val editor: Boolean? = null,
        override val review: Boolean? = null,
        override val slides: Boolean? = null,
    ) : MultiVoteMessage() {
        override fun copy(editor: Boolean?, review: Boolean?, slides: Boolean?): Style {
            return copy(userId = userId, editor = editor, review = review, slides = slides)
        }
    }

    @Serializable
    @SerialName("Time")
    data class Time(
        override val userId: String,
        override val editor: Boolean? = null,
        override val review: Boolean? = null,
        override val slides: Boolean? = null,
    ) : MultiVoteMessage() {
        override fun copy(editor: Boolean?, review: Boolean?, slides: Boolean?): Time {
            return copy(userId = userId, editor = editor, review = review, slides = slides)
        }
    }
}
