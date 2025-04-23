
pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        maven {
            credentials {
                username = "asmaa_fattah"
                password = "131313@Gmail.com"
            }
            url = uri("https://repositories.tomtom.com/artifactory/maven")
        }
        mavenCentral()
        gradlePluginPortal()
    }

}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
       // tomtomArtifactory()
        google()
        mavenCentral()
        gradlePluginPortal()
                maven { url = uri("https://jitpack.io") }
        maven {
            credentials {
                username = "asmaa_fattah"
                password = "131313@Gmail.com"
            }
            url = uri("https://repositories.tomtom.com/artifactory/maven")
        }

     //   jcenter()

    }

}


rootProject.name = "MedicineRemainder"
include(":app")
 