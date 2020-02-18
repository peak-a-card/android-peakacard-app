object Versions {
    // Android
    const val androidMinSdkVersion = 23
    const val androidTargetSdkVersion = 29
    const val androidCompileSdkVersion = 29

    // Kotlin
    const val kotlin_version = "1.3.61"

    const val gradleVersionsPlugin = "0.27.0"
    const val gradleAndroidPlugin = "4.0.0-alpha09"

    const val appCompatVersion = "1.1.0"
    const val materialVersion = "1.2.0-alpha04"
    const val recyclerviewVersion = "1.1.0"
    const val emojiBundledVersion = "1.0.0"
}

object PluginDependencies {
    const val android = "com.android.tools.build:gradle:${Versions.gradleAndroidPlugin}"
    const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin_version}"
}

object ProjectDependencies {
    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin_version}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompatVersion}"
    const val material = "com.google.android.material:material:${Versions.materialVersion}"
    const val recyclerview = "androidx.recyclerview:recyclerview:${Versions.recyclerviewVersion}"
    const val emojiBundled = "androidx.emoji:emoji-bundled:${Versions.emojiBundledVersion}"
}