package dev.bnorm.librettist.text

import kotlinx.coroutines.flow.Flow
import kotlin.time.Duration

actual fun String.flowDiff(
    other: String,
    charDelay: Duration
): Flow<String> = flowLines(other, charDelay) // TODO replace with actual diff algo
