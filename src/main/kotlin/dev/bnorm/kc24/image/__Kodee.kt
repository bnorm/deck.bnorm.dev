package dev.bnorm.kc24.image

import androidx.compose.ui.graphics.vector.ImageVector
import dev.bnorm.kc24.image.kodee.Greeting
import dev.bnorm.kc24.image.kodee.InLove
import dev.bnorm.kc24.image.kodee.Jumping
import dev.bnorm.kc24.image.kodee.`KodeeAssetsDigitalKodee-jumping copy`
import dev.bnorm.kc24.image.kodee.`KodeeAssetsDigitalKodee-naughty`
import dev.bnorm.kc24.image.kodee.Petite
import dev.bnorm.kc24.image.kodee.Regular
import dev.bnorm.kc24.image.kodee.`KodeeAssetsDigitalKodee-sharing`
import dev.bnorm.kc24.image.kodee.Sitting
import dev.bnorm.kc24.image.kodee.Small
import dev.bnorm.kc24.image.kodee.`KodeeAssetsDigitalKodee-waving`
import dev.bnorm.kc24.image.kodee.`KodeeAssetsDigitalKodee-winter`
import kotlin.collections.List

public object Kodee

private var __AllIcons: List<ImageVector>? = null

public val dev.bnorm.kc24.image.Kodee.AllIcons: List<ImageVector>
    get() {
        if (dev.bnorm.kc24.image.__AllIcons != null) {
            return dev.bnorm.kc24.image.__AllIcons!!
        }
        dev.bnorm.kc24.image.__AllIcons = listOf(
            Petite,
            `KodeeAssetsDigitalKodee-naughty`,
            Small,
            `KodeeAssetsDigitalKodee-winter`,
            InLove,
            `KodeeAssetsDigitalKodee-waving`,
            Jumping,
            `KodeeAssetsDigitalKodee-sharing`,
            Regular,
            Greeting,
            Sitting,
            `KodeeAssetsDigitalKodee-jumping copy`
        )
        return dev.bnorm.kc24.image.__AllIcons!!
    }
