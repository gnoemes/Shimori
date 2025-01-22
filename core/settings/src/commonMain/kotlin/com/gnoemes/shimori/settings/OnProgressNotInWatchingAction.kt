package com.gnoemes.shimori.settings

@JvmInline
value class OnProgressNotInWatchingAction private constructor(val value: Int) {
    companion object {
        val Nothing = OnProgressNotInWatchingAction(0)
        val MoveToWatching = OnProgressNotInWatchingAction(1)
        val OpenTrackEdit = OnProgressNotInWatchingAction(2)

        fun from(value: Int): OnProgressNotInWatchingAction {
            return when (value) {
                0 -> Nothing
                1 -> MoveToWatching
                else -> OpenTrackEdit
            }
        }
    }
}