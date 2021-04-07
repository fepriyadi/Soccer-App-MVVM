package id.android.soccerapp.ui.events.states

import id.android.soccerapp.model.EventsItem

sealed class EventListState {
    abstract val data: List<EventsItem>
    abstract val loadedAllItems: Boolean
}

data class DefaultState(override val data: List<EventsItem>, override val loadedAllItems: Boolean) : EventListState()
data class LoadingState(override val data: List<EventsItem>, override val loadedAllItems: Boolean) : EventListState()
data class ErrorState(val errorMessage: String, override val data: List<EventsItem>, override val loadedAllItems: Boolean) : EventListState()
data class EmptyState(override val data: List<EventsItem>, override val loadedAllItems: Boolean) : EventListState()