plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id ("kotlin-parcelize")
    id ("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id("com.google.dagger.hilt.android")
}

buildscript {
    dependencies {
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.51.1")
    }
}

android {
    namespace = "com.example.quickmart"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.quickmart"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }

}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    // nav graph implementation
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    implementation("androidx.databinding:databinding-runtime:8.4.1")

    // firebase
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))

    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    implementation ("com.facebook.android:facebook-android-sdk:latest.release")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // material design
    implementation ("com.google.android.material:material:1.12.0")

//    // dagger:
//    implementation ("com.google.dagger:dagger:2.51.1")
//    kapt ("com.google.dagger:dagger-compiler:2.51.1")

    val lifecycle_version = "2.8.0"

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    implementation ("androidx.lifecycle:lifecycle-common-java8:2.8.0")

    //retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")

    //gson
    implementation ("com.google.code.gson:gson:2.10.1")

    //room
    val room_version = "2.6.1"

    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")

    // coroutine
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    // hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    //kapt ("com.google.dagger:hilt-compiler:2.51.1")


    implementation ("androidx.fragment:fragment-ktx:1.7.1")

    // glide image loader
    implementation ("com.github.bumptech.glide:glide:4.14.2")
    kapt("com.github.bumptech.glide:compiler:4.14.2")

    // image slider
    implementation ("com.github.denzcoskun:ImageSlideshow:0.1.2")

    //circle imageview
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    // extendable text view
    implementation ("io.github.glailton.expandabletextview:expandabletextview:1.0.4")


    // Shimmer Effect
    implementation ("com.facebook.shimmer:shimmer:0.5.0")

}
// Allow references to generated code
kapt {
    correctErrorTypes = true
}