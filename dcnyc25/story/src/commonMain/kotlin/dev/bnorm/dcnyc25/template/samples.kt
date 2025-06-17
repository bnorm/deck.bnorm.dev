package dev.bnorm.dcnyc25.template

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.dcnyc25.CodeString
import dev.bnorm.storyboard.easel.assist.Caption
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


// TODO make examples truly dynamic so i could completely change them... live?!
//  - pull these out into a caption that allows edits
val FirstSample = CodeString(
    """
        fun main() {
          println("Hello, KotlinConf!")
        }
    """.trimIndent()
)

val SecondSample = CodeString(
    """
        fun main() {
          println("Hello, droidcon!")
        }
    """.trimIndent()
)

val ThirdSample = CodeString(
    $$"""
        fun main() {
          val greeting = "Hello"
          println("$greeting, droidcon!")
        }
    """.trimIndent()
)

fun SampleCaption(): Caption {
    return Caption {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp),
        ) {
            val first = rememberTextFieldState(FirstSample.text.text)
            val second = rememberTextFieldState(SecondSample.text.text)
            val third = rememberTextFieldState(ThirdSample.text.text)

            LaunchedEffect(Unit) {
                snapshotFlow { first.text }
                    .debounce(300)
                    .onEach { FirstSample.update(it.toString()) }
                    .launchIn(this)
                snapshotFlow { second.text }
                    .debounce(300)
                    .onEach { SecondSample.update(it.toString()) }
                    .launchIn(this)
                snapshotFlow { third.text }
                    .debounce(300)
                    .onEach { ThirdSample.update(it.toString()) }
                    .launchIn(this)
            }

            TextField(first)
            TextField(second)
            TextField(third)
        }
    }
}
