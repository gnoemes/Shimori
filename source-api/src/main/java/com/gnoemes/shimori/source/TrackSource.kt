package com.gnoemes.shimori.source

import com.gnoemes.shimori.data.core.sources.TrackDataSource
import com.gnoemes.shimori.data.core.sources.UserDataSource

/**
 * Source for tracking anime, manga, ranobe progress
 */
interface TrackSource : Source {
    val userDataSource: UserDataSource
    val trackDataSource: TrackDataSource
}