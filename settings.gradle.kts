//fun RepositoryHandler.tomtomArtifactory() {
//    maven("https://repositories.tomtom.com/artifactory/maven") {
//        content { includeGroupByRegex("com\\.tomtom\\..+") }
//    }
//}
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
       // tomtomArtifactory()
        google()
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
        }
//        maven { url = uri("https://repo.tomtom.com/artifactory/gradle")
//
//            credentials {
//                username = "asmaa_fattah"
//                password = "131313@Gmail.com"
//            }
//        } // Required for TomTom SDK

        jcenter()

    }

}


rootProject.name = "MedicineRemainder"
include(":app")
 