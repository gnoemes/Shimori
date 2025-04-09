package com.gnoemes.shimori.source.track

import com.gnoemes.shimori.source.Source

/**
 * Source for tracking anime, manga, ranobe progress
 */
interface TrackSource : Source {
    val userDataSource: UserDataSource
    val trackDataSource: TrackDataSource
}