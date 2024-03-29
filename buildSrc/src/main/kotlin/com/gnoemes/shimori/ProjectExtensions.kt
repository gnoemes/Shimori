package com.gnoemes.shimori

import com.android.build.api.dsl.ApplicationDefaultConfig
import com.android.build.api.dsl.LibraryDefaultConfig
import org.gradle.api.Project
import org.jetbrains.kotlin.konan.properties.Properties
import org.jetbrains.kotlin.konan.properties.loadProperties

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
    val version = loadProperties(path)
    // safety defaults in case file is missing
    if (version["major"] == null) version["major"] = 1
    if (version["minor"] == null) version["minor"] = 0
    if (version["patch"] == null) version["patch"] = 0

    return version
}