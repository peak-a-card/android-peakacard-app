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
  api(project(":core"))
  implementation("org.jetbrains.kotlin:kotlin-stdlib")

  api("androidx.appcompat:appcompat")
  api("com.google.android.material:material")
  api("androidx.recyclerview:recyclerview")

  api("androidx.lifecycle:lifecycle-viewmodel-ktx")
  api("androidx.core:core-ktx")

  api("com.google.firebase:firebase-auth")
  api("com.google.android.gms:play-services-auth")

  api("androidx.emoji:emoji-bundled")
  api("com.github.razir.progressbutton:progressbutton")
  api("com.xwray:groupie")
  api("com.xwray:groupie-kotlin-android-extensions")
}
