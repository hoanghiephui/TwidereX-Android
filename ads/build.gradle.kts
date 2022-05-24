plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.compose") version Versions.compose_jb

}

android {
    compileSdk = 32

    defaultConfig {
        minSdk = 21
        targetSdk = 32

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.6.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    api("com.applovin:applovin-sdk:11.4.1")
    api("com.google.android.gms:play-services-ads-identifier:18.0.1")
    api("androidx.constraintlayout:constraintlayout:2.1.3")
    api(compose.runtime)
    api(compose.foundation)
    api(compose.material)
    api(compose.materialIconsExtended)
    implementation("androidx.compose.ui:ui-tooling:1.1.1")
}