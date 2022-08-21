plugins {
    id("io.gitlab.arturbosch.detekt")
}

detekt {
    source.setFrom(
        "src/main/kotlin",
        "src/main/java"
    )
}