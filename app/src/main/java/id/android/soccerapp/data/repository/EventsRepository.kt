package id.android.soccerapp.data.repository
import id.android.soccerapp.data.db.FavoriteEvent
import id.android.soccerapp.model.EventsItem
import id.android.soccerapp.model.LeaguesItem
import io.reactivex.Single

interface EventsRepository {
    fun getPrevMatch(id: String?): Single<List<EventsItem>>

    fun getNextMatch(id: String?): Single<List<EventsItem>>

    fun getDetailMatch(id: String): Single<List<EventsItem>>

    fun getFavList(): Single<List<FavoriteEvent>>

    fun getAllLeague() : Single<List<LeaguesItem>>

    fun searchEvent(name : String?) : Single<List<EventsItem>>

}