plugins {
  `kotlin-dsl`
  `maven-publish`
}
repositories {
  google()
  mavenCentral()
}

dependencies {
  implementation("com.android.tools.build:gradle:8.1.4")
  implementation("com.google.dagger:hilt-android-gradle-plugin:2.54")
  implementation("com.palantir.gradle.gitversion:gradle-git-version:3.1.0")

  val kotlinVersion = "2.1.0"

  implementation("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:$kotlinVersion-1.0.29")
  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
  implementation("org.jetbrains.kotlin.plugin.compose:org.jetbrains.kotlin.plugin.compose.gradle.plugin:$kotlinVersion")
}
