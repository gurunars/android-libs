buildscript {
    // https://developer.android.com/jetpack/androidx/releases/compose-compiler
    val composeCompilerVersion by extra("1.5.3")
    // https://developer.android.com/jetpack/androidx/releases/compose-ui
    val composeVersion by extra("1.5.4")
    val material3Version by extra("1.1.2")
    val kotlinVersion by extra("1.9.10")
    val hiltVersion by extra("2.48")
    val timber by extra("com.jakewharton.timber:timber:5.0.1")
    val roomVersion by extra("2.6.0")

    val topProjectName by extra("com.gurunars.flashcards")

    repositories {
        google()
        mavenCentral()
    }

}
