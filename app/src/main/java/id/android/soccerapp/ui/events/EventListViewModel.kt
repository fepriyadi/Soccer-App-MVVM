package id.android.soccerapp.ui.events

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.android.soccerapp.data.db.FavoriteEvent
import id.android.soccerapp.data.repository.EventsRepository
import id.android.soccerapp.di.module.SCHEDULER_MAIN_THREAD
import id.android.soccerapp.model.EventsItem
import id.android.soccerapp.model.EventstatsItem
import id.android.soccerapp.model.LeaguesItem
import id.android.soccerapp.model.LineupItem
import id.android.soccerapp.ui.events.states.*
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import timber.log.Timber.d
import javax.inject.Inject
import javax.inject.Named

class EventListViewModel
@Inject constructor(private val repo: EventsRepository, @Named(SCHEDULER_MAIN_THREAD) val mainScheduler: Scheduler) : ViewModel() {

    val stateLiveData = MutableLiveData<EventListState>()
    val stateLeagueLiveData = MutableLiveData<LeagueListState>()
    val stateFavLiveData = MutableLiveData<FavouriteListState>()

    init {
        stateFavLiveData.value = LoadingFavState(emptyList(), emptyList(), false)
        stateLeagueLiveData.value = LoadingLeagueState(emptyList(), false)
    }

    fun updateFavList(){
        getFavList()
    }

    fun updateNextMatch(id: String?){
        stateLiveData.value = LoadingState(emptyList(), emptyList(), emptyList(), false)
        getNextMatch(id)
    }

    fun updatePrevMatch(id: String?) {
        stateLiveData.value = LoadingState(emptyList(), emptyList(), emptyList(), false)
        getPrevMatch(id)
    }

    fun updateSearchMatch(name: String?) {
        stateLiveData.value = LoadingState(emptyList(), emptyList(), emptyList(), false)
        searchMatch(name)
    }

    fun updateLeague() {
        getAllLeague()
    }

    fun updateEventDetail(id: String) {
        getDetailMatch(id)
        getEventLineup(id)
        getEventStat(id)
    }

    fun restoreEventDetail() {
        stateLiveData.value =
            DefaultState(obtainCurrentData(), obtainCurrentLineup(), obtainCurrentStat(), true)
    }

    fun restoreFavList() {
        stateFavLiveData.value = DefaultFavState(obtainCurrentFavData(), emptyList(), true)
    }

    fun restoreEventList() {
        stateLiveData.value =
            DefaultState(obtainCurrentData(), obtainCurrentLineup(), obtainCurrentStat(), true)
    }

    fun restoreLeague() {
        stateLeagueLiveData.value = DefaultLeagueState(obtainCurrentLeagueData(), false)
    }

    fun refreshEventPrevMatch(id: String?) {
        stateLiveData.value = LoadingState(emptyList(), emptyList(), emptyList(), false)
        getPrevMatch(id)
    }

    fun refreshEventNextMatch(id: String?) {
        stateLiveData.value = LoadingState(emptyList(), emptyList(), emptyList(), false)
        getNextMatch(id)
    }

    fun refreshSearchEvent(name: String?) {
        stateLiveData.value = LoadingState(emptyList(), emptyList(), emptyList(), false)
        searchMatch(name)
    }

    fun refreshFavList() {
        stateFavLiveData.value = LoadingFavState(emptyList(), emptyList(),false)
        getFavList()
    }

    fun refreshLeague() {
        stateLeagueLiveData.value = LoadingLeagueState(emptyList(), false)
        getAllLeague()
    }

    private fun getFavList() {
        repo.getFavList()
                .subscribeOn(Schedulers.io())
                .observeOn(mainScheduler)
                .subscribe(this::onFavListReceived, this::onErrorFav)
    }

    private fun getDetailMatch(id : String){
        repo.getDetailMatch(id)
            .subscribeOn(Schedulers.io())
            .observeOn(mainScheduler)
            .subscribe(this::onEventsReceived, this::onError)
    }

    private fun getPrevMatch(id: String?) {
        repo.getPrevMatch(id)
            .subscribeOn(Schedulers.io())
            .observeOn(mainScheduler)
            .subscribe(this::onEventsReceived, this::onError)
    }

    private fun getEventLineup(id: String?) {
        repo.getEventLineup(id)
            .subscribeOn(Schedulers.io())
            .observeOn(mainScheduler)
            .subscribe(this::onLineupReceived, this::onError)
    }

    private fun getEventStat(id: String?) {
        repo.getEventStat(id)
            .subscribeOn(Schedulers.io())
            .observeOn(mainScheduler)
            .subscribe(this::onStatsReceived, this::onError)
    }


    private fun getNextMatch(id: String?) {
        repo.getNextMatch(id)
            .subscribeOn(Schedulers.io())
            .observeOn(mainScheduler)
            .subscribe(this::onEventsReceived, this::onError)
    }

    private fun getAllLeague() {
        repo.getAllLeague()
                .subscribeOn(Schedulers.io())
                .observeOn(mainScheduler)
                .subscribe(this::onLeagueReceived, this::onErrorLeague)
    }

    private fun searchMatch(name: String?) {
        repo.searchEvent(name)
                .subscribeOn(Schedulers.io())
                .observeOn(mainScheduler)
                .subscribe(this::onEventsReceived, this::onError)
    }


    private fun onError(error: Throwable) {
        error.printStackTrace()
        d(error.localizedMessage)
        stateLiveData.value = error.message?.let {
            ErrorState(it,
                obtainCurrentData(),
                obtainCurrentLineup(),
                obtainCurrentStat(),
                false)
        }
    }

    private fun onErrorLeague(error: Throwable) {
        stateLeagueLiveData.value =
            error.message?.let { ErrorLeagueState(it, obtainCurrentLeagueData(), false) }
    }

    private fun onErrorFav(error: Throwable) {
        stateFavLiveData.value =
            error.message?.let { ErrorFavState(it, obtainCurrentFavData(), emptyList(), false) }
    }

    private fun onFavListReceived(favList: List<FavoriteEvent>) {
        val currentFav = obtainCurrentFavData().toMutableList()
        currentFav.addAll(favList)
        if (currentFav.isEmpty())
            stateFavLiveData.value = EmptyFavState(currentFav, emptyList(),true)
        else
            stateFavLiveData.value = DefaultFavState(currentFav, emptyList(),true)
    }

    private fun onLeagueReceived(leagues: List<LeaguesItem>) {
        val currentLeague = obtainCurrentLeagueData().toMutableList()
        currentLeague.addAll(leagues)
        stateLeagueLiveData.value = DefaultLeagueState(currentLeague, true)
    }

    private fun onEventsReceived(events: List<EventsItem>) {
        val currentEvent = obtainCurrentData().toMutableList()
        currentEvent.addAll(events)
        stateLiveData.value = DefaultState(currentEvent, emptyList(), emptyList(), true)
    }

    private fun onLineupReceived(events: List<LineupItem>) {
        val currentEvent = obtainCurrentLineup().toMutableList()
        currentEvent.addAll(events)
        stateLiveData.value = DefaultState(emptyList(), currentEvent, emptyList(), true)
    }

    private fun onStatsReceived(events: List<EventstatsItem>) {
        val currentEvent = obtainCurrentStat().toMutableList()
        currentEvent.addAll(events)
        stateLiveData.value = DefaultState(emptyList(), emptyList(), currentEvent, true)
    }

    private fun obtainCurrentData() = stateLiveData.value?.data ?: emptyList()

    private fun obtainCurrentLineup() = stateLiveData.value?.dataLineup ?: emptyList()

    private fun obtainCurrentStat() = stateLiveData.value?.dataStat ?: emptyList()

    private fun obtainCurrentLeagueData() = stateLeagueLiveData.value?.data ?: emptyList()

    private fun obtainCurrentFavData() = stateFavLiveData.value?.data ?: emptyList()

    private fun obtainCurrentLoadedAllItems() = stateLiveData.value?.loadedAllItems ?: false
}
