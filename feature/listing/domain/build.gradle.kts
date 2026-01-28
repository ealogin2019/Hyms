plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.hyms.feature.listing.domain"
    compileSdk = 34

    defaultConfig {
        minSdk = 26
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(libs.kotlinx.coroutines.core)
    implementation("javax.inject:javax.inject:1")
}
