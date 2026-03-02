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
        // THÊM DÒNG NÀY ĐỂ KÉO THƯ VIỆN MPAndroidChart
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "PEM"
include(":app")