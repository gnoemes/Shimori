package com.gnoemes.shimori.data.app

//minutes
object ExpiryConstants {
    private const val DAY = 24 * 60
    private const val WEEK = 24 * 60 * 7
    private const val HALF_WEEK = WEEK / 2

    const val DEFAULT = DAY
    const val TITLES_WITH_STATUS = 15
    const val TITLE_DETAILS = DAY
    const val TITLE_CHARACTERS = DAY
    const val TITLE_PERSONS = DAY
    const val CHARACTER_DETAILS = HALF_WEEK
    const val SYNC_PENDING_TASKS = 5
    const val SOURCE_SYNC = WEEK
    const val RELATED = WEEK
}


val Request.expiry
    get() = when (this) {
        //local track changes sync
        Request.SYNC_PENDING_TRACKS -> ExpiryConstants.SYNC_PENDING_TASKS
        //track list sync
        Request.ANIMES_WITH_STATUS, Request.MANGAS_WITH_STATUS, Request.RANOBE_WITH_STATUS -> ExpiryConstants.TITLES_WITH_STATUS
        Request.ANIME_DETAILS, Request.MANGA_DETAILS, Request.RANOBE_DETAILS -> ExpiryConstants.TITLE_DETAILS
        Request.ANIME_DETAILS_CHARACTERS, Request.MANGA_DETAILS_CHARACTERS, Request.RANOBE_DETAILS_CHARACTERS -> ExpiryConstants.TITLE_CHARACTERS
        //source info sync
        Request.GENRES, Request.STUDIOS -> ExpiryConstants.SOURCE_SYNC
        Request.CHARACTER_DETAILS, Request.PERSON_DETAILS -> ExpiryConstants.CHARACTER_DETAILS
        Request.ANIME_DETAILS_RELATED, Request.MANGA_DETAILS_RELATED, Request.RANOBE_DETAILS_RELATED -> ExpiryConstants.RELATED
        else -> ExpiryConstants.DEFAULT
    }