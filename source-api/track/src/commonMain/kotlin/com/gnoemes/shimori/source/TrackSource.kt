package com.gnoemes.shimori.source

import com.gnoemes.shimori.source.data.TrackDataSource
import com.gnoemes.shimori.source.data.UserDataSource

/**
 * Source for tracking anime, manga, ranobe progress
 */
interface TrackSource : Source {
    val userDataSource: UserDataSource
    val trackDataSource: TrackDataSource
}