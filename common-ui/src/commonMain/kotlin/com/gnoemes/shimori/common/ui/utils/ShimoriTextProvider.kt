package com.gnoemes.shimori.common.ui.utils

//TODO use multiplatform resource generation?

interface ShimoriTextProvider {
    fun text(id: MessageID): String
}

@JvmInline
value class MessageID private constructor(val id: Int) {
    companion object {
        val Today = MessageID(0)
        val AnonsDateFormat = MessageID(1)
        val MinuteShort = MessageID(2)
        val HourShort = MessageID(3)
        val DayShort = MessageID(4)
        val Anons = MessageID(5)
        val Ongoing = MessageID(6)
        val OngoingEpisodeFormat = MessageID(7)
        val TypeTV = MessageID(8)
        val TypeMovie = MessageID(9)
        val TypeSpecial = MessageID(10)
        val TypeMusic = MessageID(11)
        val TypeOva = MessageID(12)
        val TypeOna = MessageID(13)
        val TypeManga = MessageID(14)
        val TypeManhua = MessageID(15)
        val TypeManhwa = MessageID(16)
        val TypeOneShot = MessageID(17)
        val TypeDoujin = MessageID(18)
        val TypeNovel = MessageID(19)
        val TypeLightNovel = MessageID(20)
        val SortProgress = MessageID(21)
        val SortEpisodes = MessageID(22)
        val SortChapters = MessageID(23)
        val SortLastChanged = MessageID(24)
        val SortLastAdded = MessageID(25)
        val SortLastReleased = MessageID(26)
        val SortName = MessageID(27)
        val SortYourScore = MessageID(28)
        val SortUsersScore = MessageID(29)
        val RateWatching = MessageID(30)
        val RateReading = MessageID(31)
        val RateReWatching = MessageID(32)
        val RateReReading = MessageID(33)
        val RateOnHold = MessageID(34)
        val RatePlanned = MessageID(35)
        val RateCompleted = MessageID(36)
        val RateDropped = MessageID(37)
    }
}

operator fun ShimoriTextProvider.get(id: MessageID): String = text(id)