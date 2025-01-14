package com.gnoemes.shimori.convention

import com.android.build.api.dsl.ApplicationDefaultConfig
import com.android.build.api.dsl.LibraryDefaultConfig
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.konan.properties.Properties
import org.jetbrains.kotlin.konan.properties.loadProperties
import java.nio.file.NoSuchFileException

internal val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal val Project.compose: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("composelibs")

internal val Project.jetbrainsCompose: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("compose")

internal val Project.androidx: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("androidx")

fun Project.propOrDef(propertyName: String, defaultValue: Any): Any {
    var propertyValue = properties[propertyName]

    if (propertyValue == null) {
        val properties = Properties()
        properties.load(rootProject.file("local.properties").inputStream())
        propertyValue = properties[propertyName]
    }

    return propertyValue ?: defaultValue
}

fun Project.initConfigField(
    config: ApplicationDefaultConfig,
    field: String,
    default: Any = "",
    type: String = "String"
) {
    config.buildConfigField(
        type,
        field,
        propOrDef(field, default).toString().let {
            if (type == "String" && !it.contains('\"')) "\"" + it + "\""
            else it
        }
    )
}

fun Project.initConfigField(
    config: LibraryDefaultConfig,
    field: String,
    default: Any = "",
    type: String = "String"
) {
    config.buildConfigField(
        type,
        field,
        propOrDef(field, default).toString().let {
            if (type == "String" && !it.contains('\"')) "\"" + it + "\""
            else it
        }
    )
}

fun readVersion(path: String): Properties {
    val version = try {
        loadProperties(path)
    } catch (e: NoSuchFileException) {
        println("Project Version not found")
        Properties()
    }

    // safety defaults in case file is missing
    if (version["major"] == null) version["major"] = 1
    if (version["minor"] == null) version["minor"] = 0
    if (version["patch"] == null) version["patch"] = 0

    if (version["VERSION_CODE"] == null) version["VERSION_CODE"] = 1

    return version
}