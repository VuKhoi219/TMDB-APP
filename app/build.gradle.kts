plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.tmdb_app"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.tmdb_app"
        minSdk = 24
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
            buildConfigField("String", "TMDB_API_KEY", "\"eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NDVhYzM0MWM2ZjZjNWZkNzIyMDNkZWVmNTI4MmZjMyIsIm5iZiI6MTc2NjEzMTE2My44NjgsInN1YiI6IjY5NDUwNWRiNzdhODZiNDRhZjdhYjMwYSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.BJp5AL-lR-R4nuCVfDEP_uCAg3V8FEirnyNw7RZB2cg\"")
        }
        debug {
            buildConfigField("String", "TMDB_API_KEY", "\"eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NDVhYzM0MWM2ZjZjNWZkNzIyMDNkZWVmNTI4MmZjMyIsIm5iZiI6MTc2NjEzMTE2My44NjgsInN1YiI6IjY5NDUwNWRiNzdhODZiNDRhZjdhYjMwYSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.BJp5AL-lR-R4nuCVfDEP_uCAg3V8FEirnyNw7RZB2cg\"")
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
        buildConfig = true
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
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.ui.graphics)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation("com.airbnb.android:lottie:6.3.0")

    implementation("com.airbnb.android:lottie-compose:6.7.1")
    implementation("androidx.compose.material:material-icons-extended:1.7.0")
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Retrofit & OkHttp
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.google.code.gson:gson:2.10.1")
    // Coil
    implementation("io.coil-kt:coil-compose:2.6.0")
    // youtube
//    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:chromecast-sender:0.28")
//    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
//    implementation("androidx.activity:activity-compose:1.5.1")
    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:core:13.0.0")
}