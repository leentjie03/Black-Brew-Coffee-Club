plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.finalyearproject"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.finalyearproject"
        minSdk = 26
        targetSdk = 35
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
    }
}

dependencies {

    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.material3)



    implementation ("androidx.navigation:navigation-compose:2.8.5")
    implementation ("androidx.compose.material:material:1.7.6")
    implementation ("androidx.compose.ui:ui:1.5.1")
    implementation ("androidx.compose.ui:ui-tooling:1.5.1")
    implementation (libs.androidx.compose.ui.ui)
    implementation ("androidx.compose.material:material:1.4.0")
    implementation ("androidx.compose.material:material-icons-extended")
    implementation (libs.material3)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.espresso.core)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database.ktx)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation (libs.firebase.auth.v2105)
    implementation (libs.firebase.firestore)
    implementation ("com.google.android.gms:play-services-auth:21.3.0")
    implementation(libs.firebase.firestore.ktx)
    implementation ("com.google.firebase:firebase-database-ktx:20.0.3")
    implementation ("com.google.firebase:firebase-database-ktx")
    implementation ("io.coil-kt:coil-compose:2.0.0")
    implementation ("androidx.compose.foundation:foundation:1.5.0")
    implementation ("androidx.compose.material3:material3:1.1.0")
    implementation(libs.firebase.functions.ktx)
    implementation (libs.firebase.auth.v2103)
    implementation(libs.firebase.database.ktx)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation (libs.firebase.auth.v2105)
    implementation (libs.firebase.firestore)
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.material3)



    implementation ("androidx.navigation:navigation-compose:2.8.5")
    implementation ("androidx.compose.material:material:1.7.6")
    implementation ("androidx.compose.ui:ui:1.5.1")
    implementation ("androidx.compose.ui:ui-tooling:1.5.1")
    implementation (libs.androidx.compose.ui.ui)
    implementation ("androidx.compose.material:material:1.4.0")
    implementation ("androidx.compose.material:material-icons-extended")
    implementation (libs.material3)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.espresso.core)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database.ktx)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation (libs.firebase.auth.v2105)
    implementation (libs.firebase.firestore)

    implementation ("com.stripe:stripe-android:20.18.0")



    implementation(libs.firebase.firestore.ktx)
    implementation ("com.google.firebase:firebase-database-ktx:20.0.3")
    implementation ("com.google.firebase:firebase-database-ktx")
    implementation ("io.coil-kt:coil-compose:2.0.0")
    


    // Test dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
}
