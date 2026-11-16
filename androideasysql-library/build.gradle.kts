plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    `maven-publish`
}

android {
    namespace = "p32929.androideasysql_library"
    compileSdk = 35

    defaultConfig {
        minSdk = 21
        consumerProguardFiles("proguard-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }

    publishing {
        singleVariant("release") { withSourcesJar() }
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.github.p32929"
                artifactId = "AndroidEasySQL-Library"
                version = "2.0.0"
            }
        }
    }
}
