import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  `kotlin-dsl`
}

repositories {
  jcenter()
}

tasks.withType(KotlinCompile::class.java).all {
  kotlinOptions {
    jvmTarget = "1.8"
  }
}
