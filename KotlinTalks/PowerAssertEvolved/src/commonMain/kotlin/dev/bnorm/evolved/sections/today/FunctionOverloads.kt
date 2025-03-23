package dev.bnorm.evolved.sections.today

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.ui.unit.dp
import dev.bnorm.evolved.sections.intro.BULLET_1
import dev.bnorm.evolved.sections.intro.BULLET_3
import dev.bnorm.evolved.template.HeaderAndBody
import dev.bnorm.evolved.template.code.toCode
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.core.scene
import dev.bnorm.storyboard.easel.template.RevealEach

fun StoryboardBuilder.FunctionOverloads() {
    scene(stateCount = 8) {
        HeaderAndBody {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ProvideTextStyle(MaterialTheme.typography.h5) {
                    val reveal = frame.createChildTransition { it.toState() }
                    RevealEach(reveal) {
                        item { Text("$BULLET_1 Wait, didn't that just call a new function?!") }
                        item { Text("    $BULLET_3 Yep!") }
                        item { Text("    $BULLET_3 Power-Assert produces a String diagram, and it must go somewhere.") }
                        item {
                            Row {
                                Text("$BULLET_1 Given a signature of ")
                                Text("assert(Boolean)".toCode())
                                Text(", Power-Assert will search for...")
                            }
                        }
                        item {
                            Row {
                                Text("    $BULLET_3 ")
                                Text("assert(Boolean, String)".toCode())
                            }
                        }
                        item {
                            Row {
                                Text("    $BULLET_3 ")
                                Text("assert(Boolean, () -> String)".toCode())
                            }
                        }
                        item {
                            Row {
                                Text("    $BULLET_3 ")
                                Text("assert(String)".toCode())
                            }
                        }
                        item {
                            Row {
                                Text("    $BULLET_3 ")
                                Text("assert(() -> String)".toCode())
                            }
                        }
                    }
                }
            }
        }
    }
}
