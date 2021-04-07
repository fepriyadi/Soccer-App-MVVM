package id.android.soccerapp.data.repository
import android.content.Context
import id.android.soccerapp.data.Services
import id.android.soccerapp.model.PlayerItem
import id.android.soccerapp.utils.isNetworkStatusAvailable
import io.reactivex.Single

class PlayerRepositoryImpl(private val service: Services,
                           private val context: Context) : PlayerRepository {

    override fun getAllPlayer(id: String?): Single<List<PlayerItem>> {
        return if (context.isNetworkStatusAvailable()) {
            service.getAllPlayerByTeamId(id)
                    .flattenAsObservable { it.player }
                    .map {
                        val event = it
                        event
                    }.toList()
        }
        else
            Single.never()
    }
}