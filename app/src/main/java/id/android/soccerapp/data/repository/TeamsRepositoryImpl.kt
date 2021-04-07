package id.android.soccerapp.data.repository
import android.content.Context
import id.android.soccerapp.data.Services
import id.android.soccerapp.data.db.FavoriteTeam
import id.android.soccerapp.data.db.database
import id.android.soccerapp.model.TeamsItem
import id.android.soccerapp.utils.isNetworkStatusAvailable
import io.reactivex.Single
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select

class TeamsRepositoryImpl(private val service: Services,
                          private val context: Context) : TeamRepository {

    override fun getFavouriteTeam(): Single<List<FavoriteTeam>> {
        return Single.create { emitter ->
            val fav: MutableList<FavoriteTeam> = mutableListOf()
            context.database.use {
                val result = select(FavoriteTeam.TABLE_FAVORITE)
                val favorite = result.parseList(classParser<FavoriteTeam>())
                fav.addAll(favorite)
            }
            emitter.onSuccess(fav)
        }
    }

    override fun getTeamById(id: String?): Single<List<TeamsItem>> {
        return if (context.isNetworkStatusAvailable()) {
            service.getTeambyId(id)
                    .flattenAsObservable { it.teams }
                    .map {
                        val event = it
                        event
                    }.toList()
        }
        else
            Single.never()
    }

    override fun getAllTeamByLeague(leagueId: String?): Single<List<TeamsItem>> {
        return if (context.isNetworkStatusAvailable()) {
            service.getAllTeamByLeague(leagueId)
                    .flattenAsObservable { it.teams }
                    .map {
                        val event = it
                        event
                    }.toList()
        } else
            Single.error(Throwable())
    }

    override fun getTeamByName(name: String?): Single<List<TeamsItem>> {
        return if (context.isNetworkStatusAvailable()) {
            service.searchTeamByName(name)
                    .flattenAsObservable { it.teams }
                    .map {
                        val event = it
                        event
                    }.toList()
        } else
            Single.error(Throwable())
    }
}