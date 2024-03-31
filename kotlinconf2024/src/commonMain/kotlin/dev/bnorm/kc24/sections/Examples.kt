package dev.bnorm.kc24.sections

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import dev.bnorm.kc24.elements.GradleFile
import dev.bnorm.kc24.elements.OutputText
import dev.bnorm.kc24.elements.gradleTextDiff
import dev.bnorm.kc24.template.ExampleCarousel
import dev.bnorm.kc24.template.SLIDE_PADDING
import dev.bnorm.kc24.template.TitleAndBody
import dev.bnorm.librettist.Highlighting
import dev.bnorm.librettist.ShowTheme
import dev.bnorm.librettist.animation.startAnimation
import dev.bnorm.librettist.animation.then
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.slideForValues
import dev.bnorm.librettist.show.toValue
import dev.bnorm.librettist.text.buildGradleKtsCodeString
import dev.bnorm.librettist.text.buildKotlinCodeString
import dev.bnorm.librettist.text.thenLineEndDiff
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

fun ShowBuilder.Examples() {
    // TODO examples
    //  - assertNotNull -> surrounding deep not null checks
    //    - make part of another example?
    //  - at this point we could compare assertTrue/assertEquals/assertNotNull as the primary toolbox

    ExampleCarousel { AnnotatedString("") to complexAssertExample }
    ComplexExpressions()
    ExampleCarousel { complexAssertExample to assertTrueExample }
    AssertTrue()
    ExampleCarousel { assertTrueExample to requireExample }
    Require()
    ExampleCarousel { requireExample to assertEqualsExample }
    AssertEquals()
}

data class ExampleState(
    val gradleIndex: Int = 0,
    val showGradle: Boolean = false,
    val showOutput: Boolean = false,
)

private fun ShowBuilder.ComplexExpressions() {
    val states = listOf(
        ExampleState(),
        ExampleState(showOutput = true),
        ExampleState(),
    )
    slideForValues(states) {
        val state = transition.createChildTransition { it.toValue(states.first(), states.last()) }

        TitleAndBody {
            Box(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.padding(SLIDE_PADDING)) {
                    Text(complexAssertExample)
                }

                OutputText(
                    text = complexAssertOutput,
                    visible = state.createChildTransition { it.showOutput },
                    modifier = Modifier.align(Alignment.BottomStart)
                )
            }
        }
    }
}

private fun ShowBuilder.AssertTrue() {
    // TODO add some transitions to walk through...
    //  - start with assert and show error for UNSAFE_CALL?
    //  - switch to assertTrue and show without diagram?

    val states = listOf(
        ExampleState(),
        ExampleState(showGradle = true),
        ExampleState(gradleIndex = 1, showGradle = true),
        ExampleState(gradleIndex = 2, showGradle = true),
        ExampleState(gradleIndex = 2),
        ExampleState(gradleIndex = 2, showOutput = true),
        ExampleState(gradleIndex = 2),
    )
    slideForValues(states) {
        val state = transition.createChildTransition { it.toValue(states.first(), states.last()) }

        TitleAndBody {
            Box(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.padding(SLIDE_PADDING)) {
                    Text(assertTrueExample)
                }

                val gradleText by state.gradleTextDiff(assertTrueGradleSequence)
                GradleFile(
                    text = gradleText,
                    visible = state.createChildTransition { it.showGradle },
                    modifier = Modifier.align(Alignment.TopEnd),
                )

                OutputText(
                    text = assertTrueOutput,
                    visible = state.createChildTransition { it.showOutput },
                    modifier = Modifier.align(Alignment.BottomStart)
                )
            }
        }
    }
}

private fun ShowBuilder.Require() {
    val states = listOf(
        ExampleState(),
        ExampleState(showGradle = true),
        ExampleState(gradleIndex = 1, showGradle = true),
        ExampleState(gradleIndex = 2, showGradle = true),
        ExampleState(gradleIndex = 2),
        ExampleState(gradleIndex = 2, showOutput = true),
        ExampleState(gradleIndex = 2),
    )
    slideForValues(states) {
        val state = transition.createChildTransition { it.toValue(states.first(), states.last()) }

        TitleAndBody {
            Box(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.padding(SLIDE_PADDING)) {
                    Text(requireExample)
                }

                val gradleText by state.gradleTextDiff(requireGradleSequence)
                GradleFile(
                    text = gradleText,
                    visible = state.createChildTransition { it.showGradle },
                    modifier = Modifier.align(Alignment.TopEnd),
                )

                OutputText(
                    text = requireOutput,
                    visible = state.createChildTransition { it.showOutput },
                    modifier = Modifier.align(Alignment.BottomStart)
                )
            }
        }
    }
}

private fun ShowBuilder.AssertEquals() {
    val states = listOf(
        ExampleState(),
        ExampleState(showGradle = true),
        ExampleState(gradleIndex = 1, showGradle = true),
        ExampleState(gradleIndex = 1),
        ExampleState(gradleIndex = 1, showOutput = true),
        ExampleState(gradleIndex = 1),
    )
    slideForValues(states) {
        val state = transition.createChildTransition { it.toValue(states.first(), states.last()) }

        TitleAndBody {
            Box(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.padding(SLIDE_PADDING)) {
                    Text(assertEqualsExample)
                }

                val gradleText by state.gradleTextDiff(assertEqualsGradleSequence)
                GradleFile(
                    text = gradleText,
                    visible = state.createChildTransition { it.showGradle },
                    modifier = Modifier.align(Alignment.TopEnd),
                )

                OutputText(
                    text = assertEqualsOutput,
                    visible = state.createChildTransition { it.showOutput },
                    modifier = Modifier.align(Alignment.BottomStart)
                )
            }
        }
    }
}

// ======================= //
// ===== Text Values ===== //
// ======================= //

private fun String.toExampleStyle(codeStyle: Highlighting): SpanStyle? {
    return when (this) {
        "fellowshipOfTheRing", "name", "age", "race" -> codeStyle.property
        "indices" -> codeStyle.property // TODO extension properties
        "`test members of the fellowship`", "get" -> codeStyle.functionDeclaration // TODO "get" not working?
        "find", "any" -> codeStyle.extensionFunctionCall
        "assertTrue", "assertEquals", "assertSoftly", "require" -> codeStyle.staticFunctionCall
        else -> null
    }
}

private fun String.toGradleKtsStyle(codeStyle: Highlighting): SpanStyle? {
    return when (this) {
        "class" -> codeStyle.keyword
        "ExperimentalKotlinGradlePluginApi" -> codeStyle.annotation
        "functions", "includedSourceSets" -> codeStyle.property
        "kotlin", "version", "powerAssert" -> codeStyle.extensionFunctionCall
        else -> null
    }
}

// region <Complex assert Example>
private val complexAssertExample: AnnotatedString
    @Composable
    get() {
        val codeStyle = ShowTheme.code
        return remember {
            buildKotlinCodeString(
                // language=kotlin
                text = """
                    @Test
                    fun `test members of the fellowship`() {
                        val members = fellowshipOfTheRing.getCurrentMembers()
                        assert(members.any { it.name == "Boromir" } &&
                                members.any { it.name == "Aragorn" } ||
                                members.any { it.name == "Elrond" })
                    }
                """.trimIndent(),
                codeStyle = codeStyle,
                identifierType = {
                    it.toExampleStyle(codeStyle) ?: when (it) {
                        "assert" -> codeStyle.staticFunctionCall
                        else -> null
                    }
                }
            )
        }
    }
// endregion

// region <Complex assert Output>
private val complexAssertOutput = """
java.lang.AssertionError: Assertion failed
assert(members.any { it.name == "Boromir" } &&
       |       |
       |       false
       [Frodo, Sam, Merry, Pippin, Gandalf, Aragorn, Legolas, Gimli]
        members.any { it.name == "Aragorn" } ||
        members.any { it.name == "Elrond" })
        |       |
        |       false
        [Frodo, Sam, Merry, Pippin, Gandalf, Aragorn, Legolas, Gimli]
    at [...]
""".trimIndent()
// endregion

// region <assertTrue Example>
private val assertTrueExample: AnnotatedString
    @Composable
    get() {
        val codeStyle = ShowTheme.code
        return remember {
            buildKotlinCodeString(
                // language=kotlin
                text = """
                    @Test
                    fun `test members of the fellowship`() {
                        val members = fellowshipOfTheRing.getCurrentMembers()
                        val aragorn = members.find { it.name == "Aragorn" }
                        assertTrue(aragorn != null)
                        assertTrue(aragorn.age < 60)
                    }
                """.trimIndent(),
                codeStyle = codeStyle,
                identifierType = { it.toExampleStyle(codeStyle) }
            )
        }
    }
// endregion

//region <assertTrue Gradle>
private val assertTrueGradleSequence: ImmutableList<ImmutableList<AnnotatedString>>
    @Composable
    get() {
        val codeStyle = ShowTheme.code
        fun build(text: String) = buildGradleKtsCodeString(
            text = text,
            codeStyle = codeStyle,
            identifierType = { it.toGradleKtsStyle(codeStyle) }
        )

        return remember {
            persistentListOf(
                startAnimation(
                    build(
                        """
                            plugins {
                                kotlin("jvm") version "2.0.0"
                                kotlin("plugin.power-assert") version "2.0.0"
                            }
                        """.trimIndent() + "\n\n\n\n\n\n\n",
                    )
                ).thenLineEndDiff(
                    build(
                        """
                            plugins {
                                kotlin("jvm") version "2.0.0"
                                kotlin("plugin.power-assert") version "2.0.0"
                            }
                            
                            @OptIn(ExperimentalKotlinGradlePluginApi::class)
                            powerAssert {
                            }
                        """.trimIndent() + "\n\n\n",
                    )
                ).toList(),

                startAnimation(
                    build(
                        """
                            plugins {
                                kotlin("jvm") version "2.0.0"
                                kotlin("plugin.power-assert") version "2.0.0"
                            }
                            
                            @OptIn(ExperimentalKotlinGradlePluginApi::class)
                            powerAssert {
                            }
                        """.trimIndent() + "\n\n\n",
                    )
                ).then(
                    build(
                        """
                            plugins {
                                kotlin("jvm") version "2.0.0"
                                kotlin("plugin.power-assert") version "2.0.0"
                            }
                            
                            @OptIn(ExperimentalKotlinGradlePluginApi::class)
                            powerAssert {
                            
                            }
                        """.trimIndent() + "\n\n",
                    )
                ).then(
                    build(
                        """
                            plugins {
                                kotlin("jvm") version "2.0.0"
                                kotlin("plugin.power-assert") version "2.0.0"
                            }
                            
                            @OptIn(ExperimentalKotlinGradlePluginApi::class)
                            powerAssert {
                            
                            
                            }
                        """.trimIndent() + "\n",
                    )
                ).then(
                    build(
                        """
                            plugins {
                                kotlin("jvm") version "2.0.0"
                                kotlin("plugin.power-assert") version "2.0.0"
                            }
                            
                            @OptIn(ExperimentalKotlinGradlePluginApi::class)
                            powerAssert {
                            
                            
                            
                            }
                        """.trimIndent(),
                    )
                ).thenLineEndDiff(
                    build(
                        """
                            plugins {
                                kotlin("jvm") version "2.0.0"
                                kotlin("plugin.power-assert") version "2.0.0"
                            }
                            
                            @OptIn(ExperimentalKotlinGradlePluginApi::class)
                            powerAssert {
                                functions.addAll(
                                    "kotlin.test.assertTrue",
                                )
                            }
                        """.trimIndent(),
                    )
                ).toList()
            )
        }
    }
//endregion

// region <assertTrue Output>
private val assertTrueOutput = """
java.lang.AssertionError: Assertion failed
assertTrue(aragorn.age < 60)
           |       |   |
           |       |   false
           |       87
           Aragorn
    at [...]
""".trimIndent()
// endregion

// TODO
//  - surround example with a little more context?
//  - is there a better example than this?
// region <require Example>
private val requireExample: AnnotatedString
    @Composable
    get() {
        val codeStyle = ShowTheme.code
        return remember {
            buildKotlinCodeString(
                // language=kotlin
                text = """
                    operator fun get(index: Int): Character {
                        require(index in members.indices)
                        return members[index]
                    }
                """.trimIndent(),
                codeStyle = codeStyle,
                identifierType = {
                    it.toExampleStyle(codeStyle) ?: when (it) {
                        "members" -> codeStyle.property
                        else -> null
                    }
                }
            )
        }
    }
// endregion

// region <require Gradle>
private val requireGradleSequence: ImmutableList<ImmutableList<AnnotatedString>>
    @Composable
    get() {
        val codeStyle = ShowTheme.code
        fun build(text: String) = buildGradleKtsCodeString(
            text = text,
            codeStyle = codeStyle,
            identifierType = { it.toGradleKtsStyle(codeStyle) }
        )

        return remember {
            persistentListOf(
                startAnimation(
                    build(
                        """
                            plugins {
                                kotlin("jvm") version "2.0.0"
                                kotlin("plugin.power-assert") version "2.0.0"
                            }
                            
                            @OptIn(ExperimentalKotlinGradlePluginApi::class)
                            powerAssert {
                                functions.addAll(
                                    "kotlin.test.assertTrue",
                                )
                            }
                        """.trimIndent() + "\n\n",
                    )
                ).then(
                    build(
                        """
                            plugins {
                                kotlin("jvm") version "2.0.0"
                                kotlin("plugin.power-assert") version "2.0.0"
                            }
                            
                            @OptIn(ExperimentalKotlinGradlePluginApi::class)
                            powerAssert {
                                functions.addAll(
                                    
                                    "kotlin.test.assertTrue",   
                                )
                            }
                        """.trimIndent() + "\n",
                    )
                ).thenLineEndDiff(
                    build(
                        """
                            plugins {
                                kotlin("jvm") version "2.0.0"
                                kotlin("plugin.power-assert") version "2.0.0"
                            }
                            
                            @OptIn(ExperimentalKotlinGradlePluginApi::class)
                            powerAssert {
                                functions.addAll(
                                    "kotlin.require",
                                    "kotlin.test.assertTrue",
                                )
                            }
                        """.trimIndent() + "\n",
                    )
                ).toList(),

                startAnimation(
                    build(
                        """
                            plugins {
                                kotlin("jvm") version "2.0.0"
                                kotlin("plugin.power-assert") version "2.0.0"
                            }
                            
                            @OptIn(ExperimentalKotlinGradlePluginApi::class)
                            powerAssert {
                                functions.addAll(
                                    "kotlin.require",
                                    "kotlin.test.assertTrue",
                                )
                            }
                        """.trimIndent() + "\n",
                    )
                ).then(
                    build(
                        """
                            plugins {
                                kotlin("jvm") version "2.0.0"
                                kotlin("plugin.power-assert") version "2.0.0"
                            }
                            
                            @OptIn(ExperimentalKotlinGradlePluginApi::class)
                            powerAssert {
                                functions.addAll(
                                    "kotlin.require",
                                    "kotlin.test.assertTrue",
                                )
                                
                            }
                        """.trimIndent(),
                    )
                ).thenLineEndDiff(
                    build(
                        """
                            plugins {
                                kotlin("jvm") version "2.0.0"
                                kotlin("plugin.power-assert") version "2.0.0"
                            }
                            
                            @OptIn(ExperimentalKotlinGradlePluginApi::class)
                            powerAssert {
                                functions.addAll(
                                    "kotlin.require",
                                    "kotlin.test.assertTrue",
                                )
                                includedSourceSets.addAll("main", "test")
                            }
                        """.trimIndent(),
                    )
                ).toList()
            )
        }
    }
// endregion

// TODO !!! THIS IS DOCTORED OUTPUT !!!
//  - `members` prints out "FellowshipOfTheRing" as well because it is a receiver
//  - can/should we ignore implicit receivers?
//  - should "Assertion failed" as the prefix also be removed?
//  - should we show a link to tickets if these things doesn't get fixed?
// region <require Output>
private val requireOutput = """
java.lang.IllegalArgumentException: Assertion failed
require(index in members.indices)
        |     |  |       |
        |     |  |       0..8
        |     |  [Frodo, Sam, Merry, Pippin, Gandalf, Aragorn, Legolas, Gimli, Boromir]
        |     false
        10
    at [...]
""".trimIndent()
// endregion

// TODO is race a good example to use?
//  - may be a touchy topic to use during a talk.
//    - does lotr make that not a concern?
//    - man being the race is a little weird... while not technically correct, should it be human?
// region <assertEquals Example>
private val assertEqualsExample: AnnotatedString
    @Composable
    get() {
        val codeStyle = ShowTheme.code
        return remember {
            buildKotlinCodeString(
                // language=kotlin
                text = """
                    @Test
                    fun `test members of the fellowship`() {
                        val members = fellowshipOfTheRing.getAllMembers()
                        val aragorn = members.find { it.name == "Aragorn" }
                        val boromir = members.find { it.name == "Boromir" }
                        assertEquals(aragorn?.race, boromir?.race)
                    }
                """.trimIndent(),
                codeStyle = codeStyle,
                identifierType = { it.toExampleStyle(codeStyle) }
            )
        }
    }
// endregion

// region <assertEquals Gradle>
private val assertEqualsGradleSequence: ImmutableList<AnnotatedString>
    @Composable
    get() {
        val codeStyle = ShowTheme.code
        fun build(text: String) = buildGradleKtsCodeString(
            text = text,
            codeStyle = codeStyle,
            identifierType = { it.toGradleKtsStyle(codeStyle) }
        )

        return remember {
            startAnimation(
                build(
                    """
                        plugins {
                            kotlin("jvm") version "2.0.0"
                            kotlin("plugin.power-assert") version "2.0.0"
                        }
                        
                        @OptIn(ExperimentalKotlinGradlePluginApi::class)
                        powerAssert {
                            functions.addAll(
                                "kotlin.require",
                                "kotlin.test.assertTrue",
                            )
                            includedSourceSets.addAll("main", "test")
                        }
                    """.trimIndent() + "\n",
                )
            ).then(
                build(
                    """
                        plugins {
                            kotlin("jvm") version "2.0.0"
                            kotlin("plugin.power-assert") version "2.0.0"
                        }
                        
                        @OptIn(ExperimentalKotlinGradlePluginApi::class)
                        powerAssert {
                            functions.addAll(
                                "kotlin.require",
                                "kotlin.test.assertTrue",
                                
                            )
                            includedSourceSets.addAll("main", "test")
                        }
                    """.trimIndent(),
                )
            ).thenLineEndDiff(
                build(
                    """
                        plugins {
                            kotlin("jvm") version "2.0.0"
                            kotlin("plugin.power-assert") version "2.0.0"
                        }
                        
                        @OptIn(ExperimentalKotlinGradlePluginApi::class)
                        powerAssert {
                            functions.addAll(
                                "kotlin.require",
                                "kotlin.test.assertTrue",
                                "kotlin.test.assertEquals",
                            )
                            includedSourceSets.addAll("main", "test")
                        }
                    """.trimIndent(),
                )
            ).toList()
        }
    }
// endregion

// TODO !!! THIS IS DOCTORED OUTPUT !!!
//  - `assertEquals` prints on the same line as the exception name
//  - "expected:[...]" prints on the same line as "Aragorn"
//  - do we need to hardcode a newline before and after the diagram?
//  - should we show a link to a ticket if it doesn't get fixed?
// region <assertEquals Output>
private val assertEqualsOutput = """
java.lang.AssertionError: 
assertEquals(aragorn?.race, boromir?.race)
             |        |     |        |
             |        |     |        Human
             |        |     Boromir
             |        Dúnadan
             Aragorn 
expected:<Dúnadan> but was:<Human>
""".trimIndent()
// endregion
