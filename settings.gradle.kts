pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
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

rootProject.name = "Hyms"
include(":app")
include(":core:common")
include(":core:ui")
include(":core:network")
include(":core:database")
include(":core:datastore")
include(":core:security")
include(":feature:auth")
include(":feature:feed")
include(":feature:chat")
include(":feature:profile")
include(":feature:checkout")
// Updated feature:listing to be granular
include(":feature:listing:ui")
include(":feature:listing:data")
include(":feature:listing:domain")
