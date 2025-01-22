package com.gnoemes.shimori.settings

@JvmInline
value class OnProgressCompleteAction private constructor(val value: Int) {
    companion object {
        val Nothing = OnProgressCompleteAction(0)
        val MoveToComplete = OnProgressCompleteAction(1)
        val OpenTrackEdit = OnProgressCompleteAction(2)

        fun from(value: Int): OnProgressCompleteAction {
            return when (value) {
                0 -> Nothing
                1 -> MoveToComplete
                else -> OpenTrackEdit
            }
        }
    }
}