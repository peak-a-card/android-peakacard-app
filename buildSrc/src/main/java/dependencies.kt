object Versions {
    // Android
    const val androidMinSdkVersion = 23
    const val androidTargetSdkVersion = 29
    const val androidCompileSdkVersion = 29

    // Kotlin
    const val kotlin_version = "1.3.61"

    const val gradleAndroidPlugin = "4.0.0-alpha09"

    const val appCompatVersion = "1.1.0"
    const val ktxVersion = "1.2.0"
    const val materialVersion = "1.2.0-alpha05"
    const val recyclerviewVersion = "1.1.0"
    const val emojiBundledVersion = "1.0.0"
    const val timberVersion = "4.7.1"

    const val koinVersion = "2.0.1"
    const val lifecycleVersion = "2.2.0"
    const val coroutinesVersion = "1.3.3"
}

object PluginDependencies {
    const val android = "com.android.tools.build:gradle:${Versions.gradleAndroidPlugin}"
    const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin_version}"
}

object ProjectDependencies {
    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin_version}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompatVersion}"
    const val ktx = "androidx.core:core-ktx:${Versions.ktxVersion}"
    const val material = "com.google.android.material:material:${Versions.materialVersion}"
    const val recyclerview = "androidx.recyclerview:recyclerview:${Versions.recyclerviewVersion}"
    const val emojiBundled = "androidx.emoji:emoji-bundled:${Versions.emojiBundledVersion}"
    const val timber = "com.jakewharton.timber:timber:${Versions.timberVersion}"

    const val koinCore = "org.koin:koin-core:${Versions.koinVersion}"
    const val koinAndroid = "org.koin:koin-android:${Versions.koinVersion}"
    const val koinViewModel = "org.koin:koin-androidx-viewmodel:${Versions.koinVersion}"

    const val lifecycleViewModel =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleVersion}"

    const val coroutinesAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutinesVersion}"
    const val coroutinesCore =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutinesVersion}"
}