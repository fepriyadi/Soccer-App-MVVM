package id.android.soccerapp.model

data class LeaguesItem(
	val strLeagueAlternate: String? = null,
	val strLeague: String? = null,
	val strSport: String? = null,
	val idLeague: String? = null

) {
	override fun toString(): String {
		return "$strLeague"
	}
}
