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

    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = freeCompilerArgs + "-Xuse-experimental=kotlin.Experimental"
        freeCompilerArgs = freeCompilerArgs + "-Xuse-experimental=kotlinx.coroutines.FlowPreview"
        freeCompilerArgs = freeCompilerArgs + "-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi"
        freeCompilerArgs = freeCompilerArgs + "-XXLanguage:+InlineClasses"
    }
}

dependencies {
    implementation(project(":core-ui"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    implementation("androidx.dynamicanimation:dynamicanimation-ktx")
}

apply(plugin = "com.google.gms.google-services")
