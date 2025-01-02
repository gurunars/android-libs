plugins {
  `kotlin-dsl`
}
repositories {
  google()
  mavenCentral()
}

dependencies {
  implementation("com.android.tools.build:gradle:8.1.2")
  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.10")
  implementation("com.google.gms:google-services:4.4.0")
  implementation("com.google.dagger:hilt-android-gradle-plugin:2.48")
  implementation("com.google.firebase:firebase-crashlytics-gradle:2.9.9")
}
