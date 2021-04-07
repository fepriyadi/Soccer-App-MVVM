package id.android.soccerapp.ui.player
import id.android.soccerapp.model.PlayerItem

sealed class PlayerListState {
    abstract val data: List<PlayerItem>
    abstract val loadedAllItems: Boolean
}

data class DefaultPlayerState(override val data: List<PlayerItem>, override val loadedAllItems: Boolean) : PlayerListState()
data class LoadingPlayerState(override val data: List<PlayerItem>, override val loadedAllItems: Boolean) : PlayerListState()
data class ErrorPlayerState(val errorMessage: String, override val data: List<PlayerItem>, override val loadedAllItems: Boolean) : PlayerListState()