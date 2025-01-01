plugins {
  id("app-common")
}

dependencies {
  val lifecycleVersion = "2.6.2"
  implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
  implementation(project(":libs:state_machine"))
}
