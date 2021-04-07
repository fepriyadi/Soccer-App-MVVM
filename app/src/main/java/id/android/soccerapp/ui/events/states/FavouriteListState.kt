package id.android.soccerapp.ui.events.states
import id.android.soccerapp.data.db.FavoriteEvent
import id.android.soccerapp.data.db.FavoriteTeam

sealed class FavouriteListState {
    abstract val data: List<FavoriteEvent>
    abstract val favTam : List<FavoriteTeam>
    abstract val loadedAllItems: Boolean
}

data class DefaultFavState(override val data: List<FavoriteEvent>, override val favTam: List<FavoriteTeam>, override val loadedAllItems: Boolean) : FavouriteListState()
data class LoadingFavState(override val data: List<FavoriteEvent>, override val favTam: List<FavoriteTeam>, override val loadedAllItems: Boolean) : FavouriteListState()
data class ErrorFavState(val errorMessage: String, override val data: List<FavoriteEvent>, override val favTam: List<FavoriteTeam>, override val loadedAllItems: Boolean) : FavouriteListState()
data class EmptyFavState(override val data: List<FavoriteEvent>, override val favTam: List<FavoriteTeam>,override val loadedAllItems: Boolean) : FavouriteListState()