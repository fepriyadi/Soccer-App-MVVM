package id.android.soccerapp.ui.player
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.android.soccerapp.data.repository.PlayerRepository
import id.android.soccerapp.model.PlayerItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

private val TAG = PlayerListViewModel::class.java.name

class PlayerListViewModel @Inject constructor(private val repo: PlayerRepository) : ViewModel() {

    val stateLiveData = MutableLiveData<PlayerListState>()

    init {
        stateLiveData.value = LoadingPlayerState(emptyList(), false)
    }

    fun updatePlayerList(id : String?) {
        getAllPlayer(id)
    }
    fun restorePlayerList() {
        stateLiveData.value = DefaultPlayerState(obtainCurrentData(), true)
    }

    fun refreshPlayerList(id : String?) {
        stateLiveData.value = LoadingPlayerState(emptyList(), false)
        getAllPlayer(id)
    }

    private fun getAllPlayer(id : String?) {
        repo.getAllPlayer(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onPlayerListReceived, this::onError)
    }
    private fun onError(error: Throwable) {
        error.printStackTrace()
        stateLiveData.value = ErrorPlayerState(error.localizedMessage, obtainCurrentData(), false)
    }

    private fun onPlayerListReceived(playerItem: List<PlayerItem>) {
        val currentPlayers = obtainCurrentData().toMutableList()
        currentPlayers.addAll(playerItem)
        stateLiveData.value = DefaultPlayerState(currentPlayers, true)
    }

    private fun obtainCurrentData() = stateLiveData.value?.data ?: emptyList()

    private fun obtainCurrentLoadedAllItems() = stateLiveData.value?.loadedAllItems ?: false
}
