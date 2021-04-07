package id.android.soccerapp.data.repository

import id.android.soccerapp.model.PlayerItem
import io.reactivex.Single

/**
 * Created by developer on 9/19/18.
 */
interface PlayerRepository{
    fun getAllPlayer(id: String?): Single<List<PlayerItem>>

}