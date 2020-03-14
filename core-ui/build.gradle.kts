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
}

dependencies {
    api(project(":core"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("androidx.recyclerview:recyclerview")
}