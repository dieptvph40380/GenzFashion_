plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.genz_fashion"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.genz_fashion"
        minSdk = 29
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.analytics)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)


    implementation ("com.google.zxing:core:3.4.1")
    implementation ("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation ("com.google.android.material:material:1.9.0")
    implementation ("androidx.viewpager2:viewpager2:1.0.0")

    //_sdp
    implementation ("com.intuit.sdp:sdp-android:1.1.0")

    // Circle Image View ( Hỗ trợ làm ảnh tròn )
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    //Picasso để load ảnh , tất cả load ảnh trong app phải dùng Picasso
    implementation ("com.squareup.picasso:picasso:2.71828")

    // thư viện hỗ trợ pick số điện thoại theo mã vùng
    implementation ("com.hbb20:ccp:2.5.0")

    //config okHttp
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation ("com.android.volley:volley:1.2.1")
}