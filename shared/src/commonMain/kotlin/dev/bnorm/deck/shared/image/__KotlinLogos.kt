package dev.bnorm.deck.shared.image

import androidx.compose.ui.graphics.vector.ImageVector
import dev.bnorm.deck.shared.image.kotlinlogos.FullColorMark
import dev.bnorm.deck.shared.image.kotlinlogos.FullColorBlack
import dev.bnorm.deck.shared.image.kotlinlogos.MonochromeBlackMark
import dev.bnorm.deck.shared.image.kotlinlogos.MonochromeBlack
import dev.bnorm.deck.shared.image.kotlinlogos.MonochromeWhite
import dev.bnorm.deck.shared.image.kotlinlogos.OneColorMark
import dev.bnorm.deck.shared.image.kotlinlogos.OneColorWhite
import kotlin.collections.List as ____KtList

public object KotlinLogos

private var __AllIcons: ____KtList<ImageVector>? = null

public val KotlinLogos.AllIcons: ____KtList<ImageVector>
    get() {
        if (__AllIcons != null) {
            return __AllIcons!!
        }
        __AllIcons = listOf(
            FullColorBlack,
            OneColorMark,
            FullColorMark,
            MonochromeBlack,
            MonochromeWhite,
            OneColorWhite,
            MonochromeBlackMark
        )
        return __AllIcons!!
    }
