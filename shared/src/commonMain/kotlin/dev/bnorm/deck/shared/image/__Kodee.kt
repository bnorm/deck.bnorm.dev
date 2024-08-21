package dev.bnorm.deck.shared.image

import androidx.compose.ui.graphics.vector.ImageVector
import dev.bnorm.deck.shared.image.kodee.*
import kotlin.collections.List

public object Kodee

private var __AllIcons: List<ImageVector>? = null

public val Kodee.AllIcons: List<ImageVector>
    get() {
        if (__AllIcons != null) {
            return __AllIcons!!
        }
        __AllIcons = listOf(
            Petite,
            Naughty,
            Small,
            Winter,
            InLove,
            Waving,
            Jumping,
            Sharing,
            Regular,
            Greeting,
            Sitting,
            JumpingCopy
        )
        return __AllIcons!!
    }
