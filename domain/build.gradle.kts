plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
    id(libs.plugins.java.library.get().pluginId)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}
