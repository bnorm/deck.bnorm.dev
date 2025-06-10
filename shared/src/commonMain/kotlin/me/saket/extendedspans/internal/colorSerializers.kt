package me.saket.extendedspans.internal

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isUnspecified
import androidx.compose.ui.graphics.toArgb

internal fun Color?.serialize(): String {
  return if (this == null || isUnspecified) "null" else "${toArgb()}"
}

internal fun String.deserializeToColor(): Color? {
  return if (this == "null") null else Color(this.toInt())
}
