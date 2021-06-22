package id.android.soccerapp.ui.events.states

import id.android.soccerapp.model.EventsItem
import id.android.soccerapp.model.EventstatsItem
import id.android.soccerapp.model.LineupItem

sealed class EventListState {
    abstract val data: List<EventsItem>
    abstract val dataLineup: List<LineupItem>
    abstract val dataStat: List<EventstatsItem>
    abstract val loadedAllItems: Boolean
}

data class DefaultState(
    override val data: List<EventsItem>,
    override val dataLineup: List<LineupItem>,
    override val dataStat: List<EventstatsItem>,
    override val loadedAllItems: Boolean,
) : EventListState()

data class LoadingState(
    override val data: List<EventsItem>,
    override val dataLineup: List<LineupItem>,
    override val dataStat: List<EventstatsItem>,
    override val loadedAllItems: Boolean,
) : EventListState()

data class ErrorState(
    val errorMessage: String,
    override val data: List<EventsItem>,
    override val dataLineup: List<LineupItem>,
    override val dataStat: List<EventstatsItem>,
    override val loadedAllItems: Boolean,
) : EventListState()

data class EmptyState(
    override val data: List<EventsItem>,
    override val dataLineup: List<LineupItem>,
    override val dataStat: List<EventstatsItem>,
    override val loadedAllItems: Boolean,
) : EventListState()