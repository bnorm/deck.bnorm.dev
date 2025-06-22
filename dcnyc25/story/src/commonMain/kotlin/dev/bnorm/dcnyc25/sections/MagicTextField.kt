package dev.bnorm.dcnyc25.sections

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.dcnyc25.CodeString
import dev.bnorm.dcnyc25.template.*
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.assist.SceneCaption
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.easel.template.StoryEffect
import dev.bnorm.storyboard.text.magic.MagicText
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun StoryboardBuilder.MagicTextField(before: CodeString, after: CodeString) {
    scene(
        stateCount = 1,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        SceneCaption {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(16.dp),
            ) {
                TextField(FirstSample.text.text, onValueChange = {})
                TextField(SecondSample.text.text, onValueChange = {})
                TextField(ThirdSample.text.text, onValueChange = {})
                TextField(ForthSample, onValueChange = {})
                TextField(FifthSample, onValueChange = {})
            }
        }

        var asAfter by remember { mutableStateOf(false) }

        Row {
            Vertical(MaterialTheme.colors.primary) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp),
                ) {
                    val beforeState = rememberTextFieldState(before.text.text)
                    val afterState = rememberTextFieldState(after.text.text)

                    StoryEffect(Unit) {
                        snapshotFlow { beforeState.text }
                            .debounce(300)
                            .onEach { before.update(it.toString()) }
                            .launchIn(this)
                        snapshotFlow { afterState.text }
                            .debounce(300)
                            .onEach { after.update(it.toString()) }
                            .launchIn(this)
                    }

                    Box(Modifier.weight(1f)) {
                        TextSurface {
                            TextField(
                                beforeState,
                                textStyle = MaterialTheme.typography.code1,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }

                    Box {
                        Button(
                            onClick = { asAfter = !asAfter },
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colors.secondary),
                        ) {
                            Text(if (asAfter) "To Before" else "To After")
                        }
                    }

                    Box(Modifier.weight(1f)) {
                        TextSurface {
                            TextField(
                                afterState,
                                textStyle = MaterialTheme.typography.code1,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }

            Vertical(MaterialTheme.colors.secondary) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    TextSurface {
                        ProvideTextStyle(MaterialTheme.typography.code1) {
                            MagicText(
                                if (asAfter) after.text else before.text,
                                modifier = Modifier.fillMaxSize().padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
