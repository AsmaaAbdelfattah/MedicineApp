plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.medicineremainder"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.medicineremainder"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }

}

dependencies {

// Updated AndroidX libraries
    implementation("androidx.appcompat:appcompat:1.6.1") // Latest stable version
    implementation("androidx.constraintlayout:constraintlayout:2.1.4") // Latest stable version
    implementation("androidx.recyclerview:recyclerview:1.3.2") // Latest stable version
    implementation("androidx.core:core-ktx:1.12.0") // Latest stable version
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0") // Latest stable version
    implementation("androidx.activity:activity-ktx:1.9.0") // Latest stable version

    // Material Components (Jetpack Compose & Material3)
    implementation("androidx.compose.ui:ui:1.6.1") // Latest stable version
    implementation("androidx.compose.ui:ui-tooling-preview:1.6.1") // Latest stable version
    implementation("com.google.android.material:material:1.11.0") // Latest stable version

    // Firebase Dependencies
    implementation("com.google.firebase:firebase-firestore-ktx:24.10.0") // Latest stable version
    implementation("com.google.firebase:firebase-auth-ktx:22.2.0") // Latest stable version
    implementation("com.google.firebase:firebase-database-ktx:20.3.1") // Latest stable version

    // JSON Parser
    implementation("com.google.code.gson:gson:2.10.1") // Latest stable version

    // Testing dependencies
    testImplementation("junit:junit:4.13.2") // Latest stable version
    androidTestImplementation("androidx.test.ext:junit:1.1.5") // Latest stable version
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1") // Latest stable version

    // Debugging & UI Testing
    debugImplementation("androidx.compose.ui:ui-tooling:1.6.1")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.6.1")

    // Platform BOM for Compose
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))

    // Alternative to Material CalendarView (as original is outdated)
//    implementation("com.mhiew.material-calendarview:library:2.0.1") // Maintained version
//
//    // The view calendar library for Android
//    implementation("com.kizitonwose.calendar:view:2.0.1")
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")

}