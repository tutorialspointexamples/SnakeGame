plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.apnitechlab.easysnake"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.apnitechlab.easysnake"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        // Google sample AdMob IDs (safe for testing).
        // Replace in the release block with your real IDs from apps.admob.com before shipping.
        // Note: AdSense ca-pub / AMP ad slots are NOT AdMob IDs.
        manifestPlaceholders["adMobAppId"] = "ca-app-pub-3940256099942544~3347511713"
        buildConfigField(
            "String",
            "ADMOB_BANNER_ID",
            "\"ca-app-pub-3940256099942544/6300978111\""
        )
        buildConfigField(
            "String",
            "ADMOB_INTERSTITIAL_ID",
            "\"ca-app-pub-3940256099942544/1033173712\""
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // TODO: replace with your AdMob App ID + ad unit IDs before Play Store release
            // manifestPlaceholders["adMobAppId"] = "ca-app-pub-xxxxxxxx~yyyyyyyyyy"
            // buildConfigField("String", "ADMOB_BANNER_ID", "\"ca-app-pub-xxxxxxxx/yyyyyyyyyy\"")
            // buildConfigField("String", "ADMOB_INTERSTITIAL_ID", "\"ca-app-pub-xxxxxxxx/yyyyyyyyyy\"")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2024.06.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.3")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.3")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("com.google.android.gms:play-services-ads:23.2.0")

    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
