plugins {
    id("java-library")
    id("kotlin")
}

dependencies {
    implementation(platform(project(":bom")))
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
}
