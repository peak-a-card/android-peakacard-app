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

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx")

    implementation("org.koin:koin-core")
    implementation("org.koin:koin-android")
    implementation("org.koin:koin-androidx-viewmodel")

    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("androidx.appcompat:appcompat")
    implementation("androidx.core:core-ktx")
    implementation("com.google.android.material:material")
    implementation("androidx.recyclerview:recyclerview")
    implementation("androidx.emoji:emoji-bundled")
    implementation("com.jakewharton.timber:timber")
    implementation("androidx.dynamicanimation:dynamicanimation-ktx")

    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.android.gms:play-services-auth")

    implementation("com.github.razir.progressbutton:progressbutton")
}

apply(plugin = "com.google.gms.google-services")
