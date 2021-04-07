package id.android.soccerapp.ui.team

import id.android.soccerapp.model.TeamsItem

sealed class TeamListState {
    abstract val data: List<TeamsItem>
    abstract val loadedAllItems: Boolean
}

data class DefaultTeamState(override val data: List<TeamsItem>, override val loadedAllItems: Boolean) : TeamListState()
data class LoadingTeamState(override val data: List<TeamsItem>, override val loadedAllItems: Boolean) : TeamListState()
data class ErrorTeamState(val errorMessage: String, override val data: List<TeamsItem>, override val loadedAllItems: Boolean) : TeamListState()