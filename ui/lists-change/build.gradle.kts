plugins {
    id ("com.android.library")
    id ("dagger.hilt.android.plugin")
    id ("kotlin-android")
    id ("kotlin-kapt")
}

apply(from = "../../android_feature_dependencies.gradle")

dependencies {
    implementation(libs.accompanist.flowlayout)
}
