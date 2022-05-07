plugins {
    id("multiplatform-library")
    id("com.squareup.sqldelight")
}

dependencies {
    commonMainImplementation(projects.base.shared)
    commonMainImplementation(projects.data.core)
    commonMainImplementation(projects.data.paging)
    commonMainImplementation(libs.sqldelight.coroutines)

    androidMainImplementation(libs.sqldelight.driver.android)
    androidMainImplementation(libs.androidx.paging.common)
    jvmMainImplementation(libs.sqldelight.driver.jvm)
    jvmMainImplementation(libs.androidx.paging.common)
}

sqldelight {
    database("ShimoriDB") {
        packageName = com.gnoemes.shimori.Application.id + ".data.db"
        verifyMigrations = true
        sourceFolders = listOf("db")
        dialect = "sqlite:3.24"
    }
}