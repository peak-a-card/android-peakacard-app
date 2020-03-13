buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.0.0-beta02")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.70")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

task("clean", type = Delete::class) {
    delete(rootProject.buildDir)
}