package com.gnoemes.shimori.data.common

enum class RelationType(val value: String) {
    Adaptation("adaptation"),
    AlternativeSetting("alternative_setting"),
    AlternativeVersion("alternative_version"),
    Character("character"),
    FullStory("full_story"),
    Other("other"),
    ParentStory("parent_story"),
    Prequel("prequel"),
    Sequel("sequel"),
    SideStory("side_story"),
    SpinOff("spin_off"),
    Summary("summary"),
    Unknown("unknown");

    companion object {
        fun find(value: String?) = value?.let {
            entries.find { it.value == value }
        } ?: Unknown
    }
}