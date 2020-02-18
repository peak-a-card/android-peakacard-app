buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath(PluginDependencies.android)
        classpath(PluginDependencies.kotlin)
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