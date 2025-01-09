import groovy.util.Node
import java.lang.StringBuilder
import java.util.Base64

plugins {
  id("com.android.library")
  id("kotlin-android")
  id("com.google.devtools.ksp")
  id("kotlin-parcelize")
  id("maven-publish")
  id("com.palantir.git-version")
  signing
}

val name = project.projectDir.toString()
  .dropPrefix("${project.rootDir}/")
  .slashesToDots()

val topProjectName: String by rootProject.extra

val gitVersion: groovy.lang.Closure<String> by extra

android {
  compileSdk = 34
  buildToolsVersion = "33.0.1"

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
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.1")

  testImplementation("org.robolectric:robolectric:4.8")

  val hiltVersion: String by rootProject.extra

  fun hilt(name: String) = "com.google.dagger:hilt-$name:$hiltVersion"

  implementation(hilt("android"))
  ksp(hilt("compiler"))

  testImplementation(hilt("android-testing"))
  kspTest(hilt("android-compiler"))
}

// Get from git tag
val libVersion = gitVersion()

fun Node.n(name: String, inner: Node.() -> Unit) = appendNode(name).apply(inner)

fun Node.n(name: String, inner: String) = appendNode(name, inner)

fun v(name: String) = System.getenv(name)

publishing {
  publications {
    create<MavenPublication>(name) {
      groupId = topProjectName
      artifactId = name
      version = libVersion
      description = project.description
      artifact(layout.buildDirectory.file("outputs/aar/${name}-release.aar"))
      pom.withXml {
        asNode().apply {
          val deps = listOf("implementation", "api").flatMap {
            configurations.getByName(it).allDependencies
          }

          project.description?.let { n("description", it) }

          n("licenses") {
            n("license") {
              n("name", "The Apache Software License, Version 2.0")
              n("url", "http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
          }

          n("developers") {
            n("developer") {
              n("id", "gurunars")
              n("name", "Anton Berezin")
              n("email", "gurunars@gmail.com")
            }
          }

          n("dependencies") {
            for (dep in deps) {
              val group = if (dep.group == rootProject.name) topProjectName else dep.group
              val version = if (dep.version == "unspecified") libVersion else dep.version

              n("dependency") {
                n("groupId", group!!)
                n("artifactId", dep.name)
                n("version", version!!)
              }
            }
          }

        }
      }
    }
  }

  repositories {
    mavenLocal()
  }
}
// Sign: https://central.sonatype.org/publish/requirements/gpg/
// Publish: https://central.sonatype.org/publish/publish-portal-maven/
// keyid: 5C60B47B2F29D3D8D2C9289A85536EA8D87A5AEF
// Gradle: https://central.sonatype.org/publish/publish-gradle/
