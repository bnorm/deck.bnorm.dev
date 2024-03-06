package dev.bnorm.librettist.text

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

internal fun List<Flow<String>>.concat(): Flow<String> {
    return flow {
        for (flow in this@concat) {
            emitAll(flow)
        }
    }
}