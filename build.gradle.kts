buildscript {
  repositories {
    google()
    jcenter()
  }
  dependencies {
    classpath("com.android.tools.build:gradle:4.0.0-beta04")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.71")
    classpath("com.google.gms:google-services:4.3.3")
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
