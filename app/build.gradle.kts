plugins {
  id("com.android.application")
  id("kotlin-android")
  id("kotlin-android-extensions")
}

android {
  compileSdkVersion(Versions.androidCompileSdkVersion)

  defaultConfig {
    applicationId = "com.peakacard.app"
    setTargetSdkVersion(Versions.androidTargetSdkVersion)
    setMinSdkVersion(Versions.androidMinSdkVersion)
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {
  implementation(project(":core"))
  implementation(project(":core-ui"))

  implementation(ProjectDependencies.kotlinStdLib)
  implementation(ProjectDependencies.appCompat)
  implementation(ProjectDependencies.material)
  implementation(ProjectDependencies.recyclerview)
  implementation(ProjectDependencies.emojiBundled)
}
