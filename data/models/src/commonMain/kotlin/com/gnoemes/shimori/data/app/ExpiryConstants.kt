package com.gnoemes.shimori.data.app

//minutes
object ExpiryConstants {
    private const val DAY = 24 * 60
    private const val WEEK = 24 * 60 * 7
    private const val HALF_WEEK = WEEK / 2

    const val TITLES_WITH_STATUS = 15
    const val TITLE_DETAILS = DAY
    const val TITLE_CHARACTERS = DAY
    const val TITLE_PERSONS = DAY
    const val CHARACTER_DETAILS = HALF_WEEK
    const val PERSON_DETAILS = HALF_WEEK
    const val SYNC_PENDING_TASKS = 5
    const val SOURCE_SYNC = WEEK
}