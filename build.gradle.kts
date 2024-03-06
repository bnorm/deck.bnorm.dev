plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

group = "dev.bnorm"
version = "1.0-SNAPSHOT"

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)
    implementation(project(":librettist:librettist-core"))

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
