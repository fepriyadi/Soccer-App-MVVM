package id.android.soccerapp.data.repository

import id.android.soccerapp.data.db.FavoriteTeam
import id.android.soccerapp.model.TeamsItem
import io.reactivex.Single

interface TeamRepository{

    fun getTeamById(id: String?): Single<List<TeamsItem>>
    fun getAllTeamByLeague(leagueId : String?) : Single<List<TeamsItem>>
    fun getTeamByName(name : String?) : Single<List<TeamsItem>>
    fun getFavouriteTeam() : Single<List<FavoriteTeam>>
}