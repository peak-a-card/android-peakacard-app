plugins {
    id("java-library")
    id("kotlin")
}

dependencies {
    implementation(platform(project(":bom")))
    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    api("org.jetbrains.kotlinx:kotlinx-coroutines-android")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-play-services")

    api("com.jakewharton.timber:timber")

    api("org.koin:koin-core")
    api("org.koin:koin-android")
    api("org.koin:koin-androidx-viewmodel")

    api("com.google.firebase:firebase-firestore-ktx")
}
