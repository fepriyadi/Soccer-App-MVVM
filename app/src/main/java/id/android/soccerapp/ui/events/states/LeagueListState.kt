package id.android.soccerapp.ui.events.states

import id.android.soccerapp.model.LeaguesItem

sealed class LeagueListState {
    abstract val data: List<LeaguesItem>
    abstract val loadedAllItems: Boolean
}

data class DefaultLeagueState(override val data: List<LeaguesItem>, override val loadedAllItems: Boolean) : LeagueListState()
data class LoadingLeagueState(override val data: List<LeaguesItem>, override val loadedAllItems: Boolean) : LeagueListState()
data class ErrorLeagueState(val errorMessage: String, override val data: List<LeaguesItem>, override val loadedAllItems: Boolean) : LeagueListState()
data class EmptyLeagueState(override val data: List<LeaguesItem>, override val loadedAllItems: Boolean) : LeagueListState()