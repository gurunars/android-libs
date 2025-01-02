import java.lang.StringBuilder

plugins {
  id("com.android.library")
  id("kotlin-android")
  id("kotlin-kapt")
  id("dagger.hilt.android.plugin")
  id("kotlin-parcelize")
}

android {
  compileSdk = 34
  buildToolsVersion = "33.0.1"

  val topProjectName: String by rootProject.extra

  val name = project.projectDir.toString()
    .dropPrefix("${project.rootDir}/")
    .slashesToDots()

  //project.logger.error("project.name: ${name}")

  namespace = "$topProjectName.$name"

  defaultConfig {
    minSdk = 31
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  testOptions {
    unitTests {
      isIncludeAndroidResources = true
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  kotlinOptions {
    jvmTarget = "17"
  }
}

fun String.slashesToDots(): String {
  val chars = StringBuilder()
  for (char in this) {
    chars.append(
      if (char == '/') '.' else char
    )
  }
  return chars.toString()
}

fun String.dropPrefix(prefix: String): String =
  if (subSequence(0, prefix.length) == prefix) {
    subSequence(prefix.length, length).toString()
  } else {
    this
  }

dependencies {
  val timber: String by rootProject.extra

  implementation(timber)

  fun test(name: String) {
    testImplementation(name)
    androidTestImplementation(name)
  }

  test("junit:junit:4.13.2")
  test("androidx.test.ext:junit-ktx:1.2.1")
  test("androidx.test.ext:junit:1.2.1")
  test("com.google.truth:truth:1.1.3")
  test("android.arch.core:core-testing:1.1.1")
  test("org.jetbrains.kotlin:kotlin-test:1.1.51")

  testImplementation("org.robolectric:robolectric:4.8")

  val hiltVersion: String by rootProject.extra

  fun hilt(name: String) = "com.google.dagger:hilt-$name:$hiltVersion"

  implementation(hilt("android"))
  kapt(hilt("compiler"))

  testImplementation(hilt("android-testing"))
  kaptTest(hilt("android-compiler"))
  testAnnotationProcessor(hilt("android-compiler"))
}
