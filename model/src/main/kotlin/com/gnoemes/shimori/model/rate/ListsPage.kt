package com.gnoemes.shimori.model.rate

enum class ListsPage(val status: RateStatus?) {
    PINNED(null),
    WATCHING(RateStatus.WATCHING),
    RE_WATCHING(RateStatus.REWATCHING),
    ON_HOLD(RateStatus.ON_HOLD),
    PLANNED(RateStatus.PLANNED),
    COMPLETED(RateStatus.COMPLETED),
    DROPPED(RateStatus.DROPPED);

    companion object {
        fun find(status: RateStatus?) = values().find { it.status == status }
    }

}
