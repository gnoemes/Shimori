plugins {
    id("multiplatform-library")
    id("com.squareup.sqldelight")
}

dependencies {
    commonMainImplementation(projects.base.shared)
    commonMainImplementation(projects.data.base)
    commonMainImplementation(libs.sqldelight.coroutines)

    androidMainImplementation(libs.sqldelight.driver.android)
    jvmMainImplementation(libs.sqldelight.driver.jvm)
}

sqldelight {
    database("ShimoriDB") {
        packageName = com.gnoemes.shimori.Application.id + ".data.db"
        verifyMigrations = true
        sourceFolders = listOf("db")
        dialect = "sqlite:3.24"
    }
}