package id.android.soccerapp.ui.team

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.android.soccerapp.data.db.FavoriteTeam
import id.android.soccerapp.data.repository.TeamRepository
import id.android.soccerapp.model.TeamsItem
import id.android.soccerapp.ui.events.states.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

private val TAG = TeamListViewModel::class.java.name

class TeamListViewModel @Inject constructor(private val repo: TeamRepository) : ViewModel() {

    val stateLiveData = MutableLiveData<TeamListState>()
    val stateFavLiveData = MutableLiveData<FavouriteListState>()
    init {
        stateLiveData.value = LoadingTeamState(emptyList(), false)
        stateFavLiveData.value = LoadingFavState(emptyList(), emptyList(), false)
    }

    fun updateTeamInfo(id : String?) {
        getTeamInfo(id)
    }

    fun updateTeamList(id : String?) {
        getTeamByLeague(id)
    }

    fun updateTeam(name : String?) {
        getTeamByName(name)
    }

    fun restoreTeamInfo() {
        stateLiveData.value = DefaultTeamState(obtainCurrentData(), true)
    }

    fun restoreTeam() {
        stateLiveData.value = DefaultTeamState(obtainCurrentData(), true)
    }

    fun restoreTeamList() {
        stateLiveData.value = DefaultTeamState(obtainCurrentData(), true)
    }

    fun refreshTeamList(id : String?) {
        stateLiveData.value = LoadingTeamState(emptyList(), false)
        getTeamByLeague(id)
    }

    fun refreshTeam(id : String?) {
        stateLiveData.value = LoadingTeamState(emptyList(), false)
        getTeamByName(id)
    }

    private fun getTeamInfo(id : String?) {
        repo.getTeamById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onTeamInfoReceived, this::onError)
    }

    private fun getTeamByLeague(leagueId : String?) {
        repo.getAllTeamByLeague(leagueId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onTeamInfoReceived, this::onError)
    }
    private fun getTeamByName(name : String?) {
        repo.getTeamByName(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onTeamInfoReceived, this::onError)
    }

    fun updateFavList(){
        getFavList()
    }

    fun restoreFavList(){
        stateFavLiveData.value = DefaultFavState(emptyList(), obtainCurrentFavData(), true)
    }


    fun refreshFavList() {
        stateFavLiveData.value = LoadingFavState(emptyList(), emptyList(),false)
        getFavList()
    }

    private fun getFavList() {
        repo.getFavouriteTeam()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onFavListReceived, this::onErrorFav)
    }

    private fun onTeamReceived(teamInfo: List<TeamsItem>) {
        stateLiveData.value = DefaultTeamState(teamInfo, true)
    }


    private fun onFavListReceived(favList: List<FavoriteTeam>) {
        val currentFav = obtainCurrentFavData().toMutableList()
        currentFav.addAll(favList)
        if (currentFav.isEmpty())
            stateFavLiveData.value = EmptyFavState(emptyList(), currentFav,true)
        else
            stateFavLiveData.value = DefaultFavState(emptyList(), currentFav,true)
    }

    private fun onTeamInfoReceived(teamInfo: List<TeamsItem>) {
        val currentTeam = obtainCurrentData().toMutableList()
        currentTeam.addAll(teamInfo)
        stateLiveData.value = DefaultTeamState(currentTeam, true)
    }

    private fun obtainCurrentData() = stateLiveData.value?.data ?: emptyList()

    private fun onError(error: Throwable) {
        error.printStackTrace()
        stateLiveData.value = ErrorTeamState(error.localizedMessage, obtainCurrentData(), false)
    }

    private fun onErrorFav(error: Throwable) {
        stateFavLiveData.value = ErrorFavState(error.localizedMessage, emptyList() , obtainCurrentFavData(),false)
    }
    private fun obtainCurrentFavData() = stateFavLiveData.value?.favTam ?: emptyList()
    private fun obtainCurrentLoadedAllItems() = stateLiveData.value?.loadedAllItems ?: false
}
