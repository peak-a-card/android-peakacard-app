plugins {
    `java-platform`
}

dependencies {
    constraints {
        val version = object {
            val koin = "2.0.1"
            val coroutines = "1.3.3"
        }

        api("androidx.appcompat:appcompat:1.1.0")
        api("androidx.core:core-ktx:1.2.0")
        api("com.google.android.material:material:1.2.0-alpha05")
        api("androidx.recyclerview:recyclerview:1.1.0")
        api("androidx.emoji:emoji-bundled:1.0.0")
        api("com.jakewharton.timber:timber:4.7.1")

        api("org.koin:koin-core:${version.koin}")
        api("org.koin:koin-android:${version.koin}")
        api("org.koin:koin-androidx-viewmodel:${version.koin}")

        api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")

        api("org.jetbrains.kotlinx:kotlinx-coroutines-android:${version.coroutines}")
        api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${version.coroutines}")
    }
}