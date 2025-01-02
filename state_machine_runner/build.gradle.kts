plugins {
  id("lib-common")
}

dependencies {
  val lifecycleVersion = "2.8.7"
  implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
  implementation(project(":state_machine"))
}
