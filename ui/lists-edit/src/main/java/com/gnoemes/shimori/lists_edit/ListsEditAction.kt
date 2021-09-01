package com.gnoemes.shimori.lists_edit

import com.gnoemes.shimori.model.rate.RateStatus

internal sealed class ListsEditAction {
    data class StatusChanged(val newStatus : RateStatus) : ListsEditAction()
    data class ProgressChanged(val newValue : Int) : ListsEditAction()
    data class RewatchesChanged(val newValue : Int) : ListsEditAction()
    data class ScoreChanged(val newValue : Int?) : ListsEditAction()
    data class CommentEdit(val editing : Boolean) : ListsEditAction()
    data class CommentChanged(val newComment : String?) : ListsEditAction()
    object TogglePin : ListsEditAction()
    object Delete : ListsEditAction()
    object Save : ListsEditAction()
}