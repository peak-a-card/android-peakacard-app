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
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":core-ui"))

    implementation(ProjectDependencies.coroutinesAndroid)
    implementation(ProjectDependencies.coroutinesCore)

    implementation(ProjectDependencies.lifecycleViewModel)

    implementation(ProjectDependencies.koinCore)
    implementation(ProjectDependencies.koinAndroid)
    implementation(ProjectDependencies.koinViewModel)

    implementation(ProjectDependencies.kotlinStdLib)
    implementation(ProjectDependencies.appCompat)
    implementation(ProjectDependencies.ktx)
    implementation(ProjectDependencies.material)
    implementation(ProjectDependencies.recyclerview)
    implementation(ProjectDependencies.emojiBundled)
    implementation(ProjectDependencies.timber)
}
