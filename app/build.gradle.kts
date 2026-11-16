import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
if (keystorePropertiesFile.exists()) {
    keystorePropertiesFile.inputStream().use { keystoreProperties.load(it) }
}

android {
    namespace = "com.w3stech.easysnake"
    compileSdk = 36

    signingConfigs {
        create("release") {
            if (keystorePropertiesFile.exists()) {
                storeFile = rootProject.file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["storePassword"] as String
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
            }
        }
    }

    defaultConfig {
        applicationId = "com.w3stech.easysnake"
        minSdk = 26
        targetSdk = 36
        versionCode = 2
        versionName = "1.1"

        // AdMob App ID + separate banner / interstitial pools.
        // Banners rotate every 45s; interstitials preload a random unit for after game end.
        // Host app-ads.txt at your Play Console website root (see repo app-ads.txt).
        manifestPlaceholders["adMobAppId"] = "ca-app-pub-5172543149836113~9265123564"
        buildConfigField(
            "String[]",
            "ADMOB_BANNER_IDS",
            "{" +
                "\"ca-app-pub-5172543149836113/2088186170\"," +
                "\"ca-app-pub-5172543149836113/2037195229\"," +
                "\"ca-app-pub-5172543149836113/2172926590\"," +
                "\"ca-app-pub-5172543149836113/5069572738\"," +
                "\"ca-app-pub-5172543149836113/4942735172\"," +
                "\"ca-app-pub-5172543149836113/5265013857\"," +
                "\"ca-app-pub-5172543149836113/7312592690\"," +
                "\"ca-app-pub-5172543149836113/1598211527\"," +
                "\"ca-app-pub-5172543149836113/7297260761\"," +
                "\"ca-app-pub-5172543149836113/1326748788\"" +
                "}"
        )
        buildConfigField(
            "String[]",
            "ADMOB_INTERSTITIAL_IDS",
            "{" +
                "\"ca-app-pub-5172543149836113/6986269571\"," +
                "\"ca-app-pub-5172543149836113/2855452875\"," +
                "\"ca-app-pub-5172543149836113/1269928525\"," +
                "\"ca-app-pub-5172543149836113/4831054535\"," +
                "\"ca-app-pub-5172543149836113/8294501878\"," +
                "\"ca-app-pub-5172543149836113/1953434746\"," +
                "\"ca-app-pub-5172543149836113/3588970109\"," +
                "\"ca-app-pub-5172543149836113/3054938438\"," +
                "\"ca-app-pub-5172543149836113/1509601673\"," +
                "\"ca-app-pub-5172543149836113/2631111650\"" +
                "}"
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            ndk {
                // Embed native debug symbols in the AAB for Play Console crash analysis.
                debugSymbolLevel = "SYMBOL_TABLE"
            }
            signingConfig = if (keystorePropertiesFile.exists()) {
                signingConfigs.getByName("release")
            } else {
                signingConfigs.getByName("debug")
            }
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
