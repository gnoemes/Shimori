import com.android.build.gradle.internal.dsl.BaseAppModuleExtension

plugins {
    id("android-base")
}

configure<BaseAppModuleExtension> {
    packagingOptions.resources.excludes.addAll(
        arrayOf(
            // Exclude AndroidX version files
            "META-INF/*.version",
            // Exclude consumer proguard files
            "META-INF/proguard/*",
            // Exclude the Firebase/Fabric/other random properties files
            "/*.properties",
            "fabric/*.properties",
            "META-INF/*.properties",
        )
    )
}
