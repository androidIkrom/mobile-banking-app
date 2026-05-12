
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.zoomrad"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.quotereminder"
        minSdk = 25
        targetSdk = 36
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
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.hilt.work)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.work.runtime)
    implementation(libs.coil.compose)

    implementation(libs.orbit.core)
    implementation(libs.orbit.viewmodel)
    implementation(libs.orbit.compose)
    ksp(libs.room.compiler)
    implementation(libs.hilt.android)

    implementation(project(":entity"))
    implementation(project(":usecase"))
    implementation(project(":presenter"))
    implementation(project(":core"))
}

//

//    val hilt_version = "2.59.2"
//    implementation("com.google.dagger:hilt-android:$hilt_version")
//    ksp("com.google.dagger:hilt-compiler:$hilt_version")
//
//    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
//
//    implementation("org.orbit-mvi:orbit-core:10.0.0")
//    implementation("org.orbit-mvi:orbit-viewmodel:10.0.0")
//    implementation("org.orbit-mvi:orbit-compose:10.0.0")
//
//    val work_version = "2.11.2"
//    implementation("androidx.work:work-runtime-ktx:${work_version}")
//
//
//    val nav_version = "2.9.7"
//
//    implementation("androidx.navigation:navigation-compose:$nav_version")
//
//
//    val room_version = "2.8.4"
//    implementation("androidx.room:room-runtime:$room_version")
//    implementation("androidx.room:room-ktx:${room_version}")
//    ksp("androidx.room:room-compiler:$room_version")
//
//    implementation("androidx.hilt:hilt-work:1.0.0")
//    implementation("com.squareup.retrofit2:retrofit:2.9.0")
//    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
