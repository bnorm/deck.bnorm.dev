package dev.bnorm.librettist.text

import kotlinx.coroutines.flow.Flow
import kotlin.time.Duration

actual fun String.flowDiff(
    other: String,
): Flow<String> = flowLines(other) // TODO replace with actual diff algo
