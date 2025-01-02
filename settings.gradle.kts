pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "android-libs" // Your project name
include(":app_closer:abstraction")
include(":app_closer:real")
include(":app_closer:stubs")
include(":state_machine")
include(":state_machine_runner")
