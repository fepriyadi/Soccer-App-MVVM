package id.android.soccerapp.data.repository

import android.content.Context
import id.android.soccerapp.data.Services
import id.android.soccerapp.data.db.FavoriteEvent
import id.android.soccerapp.data.db.database
import id.android.soccerapp.model.EventsItem
import id.android.soccerapp.model.LeaguesItem
import id.android.soccerapp.utils.isNetworkStatusAvailable
import io.reactivex.Single
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select

class EventsRepositoryImpl(private val service: Services,
                           private val context: Context) : EventsRepository {

    override fun getAllLeague(): Single<List<LeaguesItem>> {
        return if (context.isNetworkStatusAvailable()) {
            service.getAllLeague()
                    .flattenAsObservable { it.leagues}
                    .map {
                        val event = it
                        event
                    }.toList()

        } else
            Single.error(Throwable())
    }

    override fun searchEvent(name: String?) : Single<List<EventsItem>> {
        return if (context.isNetworkStatusAvailable()) {
            service.searchEvent(name)
                    .flattenAsObservable {
                        it.event
                    }
                    .map {
                        val event = it
                        event
                    }.toList()
        } else
            Single.error(Throwable())
    }

    override fun getFavList(): Single<List<FavoriteEvent>> {
        return Single.create { emitter ->
            val fav: MutableList<FavoriteEvent> = mutableListOf()
            context.database.use {
                val result = select(FavoriteEvent.TABLE_FAVORITE)
                val favorite = result.parseList(classParser<FavoriteEvent>())
                fav.addAll(favorite)
            }
            emitter.onSuccess(fav)
        }
    }

    override fun getNextMatch(id: String?): Single<List<EventsItem>> {
        return if (context.isNetworkStatusAvailable()) {
            service.getFiveteenNextEvents(id)
                    .flattenAsObservable { it.events }
                    .map {
                        val event = it
                        event
                    }.toList()
        } else
            Single.error(Throwable())
    }

    override fun getDetailMatch(id: String): Single<List<EventsItem>> {
        return if (context.isNetworkStatusAvailable()) {
            service.getDetailEventById(id = id)
                    .flattenAsObservable { it.events }
                    .map {
                        val event = it
                        event
                    }.toList()
        } else
            Single.error(Throwable())
    }


    override fun getPrevMatch(id: String?): Single<List<EventsItem>> {
        return if (context.isNetworkStatusAvailable()) {
            service.getFiveteenLastEvents(id)
                    .flattenAsObservable {
                        it.events
                    }
                    .map {
                        val event = it
                        event
                    }.toList()
        } else
            Single.error(Throwable())
    }
}