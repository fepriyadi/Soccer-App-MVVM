package id.android.soccerapp.data

import id.android.soccerapp.model.EventsResponse
import id.android.soccerapp.model.Leagues
import id.android.soccerapp.model.Players
import id.android.soccerapp.model.TeamDetail
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface Services {
    @GET("eventsnextleague.php")
    fun getFiveteenNextEvents(@Query("id") id : String?): Single<EventsResponse>

    @GET("eventspastleague.php")
    fun getFiveteenLastEvents(@Query("id") id : String?): Single<EventsResponse>

    @GET("lookupevent.php")
    fun getDetailEventById(@Query("id") id : String?): Single<EventsResponse>

    @GET("lookupteam.php")
    fun getTeambyId(@Query("id") id : String?): Single<TeamDetail>

    @GET("all_leagues.php")
    fun getAllLeague(): Single<Leagues>

    @GET("searchevents.php")
    fun searchEvent(@Query("e") name: String?) : Single<EventsResponse>

    @GET("search_all_teams.php")
    fun getAllTeamByLeague(@Query("l") name: String?) : Single<TeamDetail>

    @GET("searchteams.php")
    fun searchTeamByName(@Query("t") name: String?) : Single<TeamDetail>

    @GET("lookup_all_players.php")
    fun getAllPlayerByTeamId(@Query("id") id: String?) : Single<Players>

}