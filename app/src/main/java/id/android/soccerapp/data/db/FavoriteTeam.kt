package id.android.soccerapp.data.db

/**
 * Created by developer on 9/24/18.
 */
data class FavoriteTeam(val id: Long?, val teamId: String?, val teamName :String?, val teamBadge: String?,
                        val teamYear :String?, val teamStadium: String?, val overview: String?) {

    companion object {
        const val TABLE_FAVORITE: String = "TABLE_FAVORITE_TEAM"
        const val ID: String = "ID_"
        const val TEAM_ID: String = "TEAM_ID"
        const val TEAM_NAME: String = "TEAM_NAME"
        const val TEAM_BADGE: String = "TEAM_BADGE"
        const val TEAM_YEAR: String = "TEAM_YEAR"
        const val TEAM_STADIUM: String = "TEAM_STADIUM"
        const val TEAM_OVERVIEW: String = "TEAM_OVERVIEW"
    }
}