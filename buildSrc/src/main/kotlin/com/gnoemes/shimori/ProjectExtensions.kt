package com.gnoemes.shimori

import com.android.build.api.dsl.ApplicationDefaultConfig
import org.gradle.api.Project
import org.jetbrains.kotlin.konan.properties.Properties

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