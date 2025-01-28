description = "Android state machine runner activity"

plugins {
  id("lib-common")
  id("org.jetbrains.kotlin.plugin.compose")
}

android {
  buildFeatures {
    compose = true
  }
}

dependencies {
  implementation(project(":state_machine"))
  implementation("androidx.activity:activity-compose:1.10.0")
  implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
}
