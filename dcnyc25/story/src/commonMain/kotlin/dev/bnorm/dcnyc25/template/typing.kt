package dev.bnorm.dcnyc25.template

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween

fun <T> typing(characters: Int): TweenSpec<T> =
    tween(durationMillis = 35 * characters, easing = LinearEasing)
