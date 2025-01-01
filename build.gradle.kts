buildscript {
    // https://developer.android.com/jetpack/androidx/releases/compose-compiler
    val composeCompilerVersion by extra("1.5.15")
    // https://developer.android.com/jetpack/androidx/releases/compose-ui
    val composeVersion by extra("1.7.6")
    val material3Version by extra("1.3.1")
    val kotlinVersion by extra("2.1.0")
    val hiltVersion by extra("2.48")

    val topProjectName by extra("com.gurunars.android_libs")

    repositories {
        google()
        mavenCentral()
    }

}
