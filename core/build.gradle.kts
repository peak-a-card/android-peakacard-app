plugins {
  id("com.android.library")
  id("kotlin-android")
  id("kotlin-android-extensions")
}

android {
  compileSdkVersion(Versions.androidCompileSdkVersion)

  defaultConfig {
    setTargetSdkVersion(Versions.androidTargetSdkVersion)
    setMinSdkVersion(Versions.androidMinSdkVersion)
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
  }

  buildTypes {
    named("release") {
      isMinifyEnabled = false
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
    }
  }

  kotlinOptions {
    jvmTarget = "1.8"
    freeCompilerArgs = freeCompilerArgs + "-Xuse-experimental=kotlin.Experimental"
    freeCompilerArgs = freeCompilerArgs + "-Xuse-experimental=kotlinx.coroutines.FlowPreview"
    freeCompilerArgs = freeCompilerArgs + "-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi"
    freeCompilerArgs = freeCompilerArgs + "-XXLanguage:+InlineClasses"
  }
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
