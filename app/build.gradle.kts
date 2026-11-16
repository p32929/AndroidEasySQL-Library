plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "p32929.databaseeasier"
    compileSdk = 35

    defaultConfig {
        applicationId = "p32929.databaseeasier"
        minSdk = 24
        targetSdk = 35
        versionCode = 2
        versionName = "2.0.0"
    }

    buildFeatures { compose = true }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }

    lint { abortOnError = false }
}

dependencies {
    implementation(project(":androideasysql-library"))
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.compose.icons)
    implementation(libs.activity.compose)
}
