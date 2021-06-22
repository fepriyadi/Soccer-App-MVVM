package id.android.soccerapp.model

data class LineupResponse(
    val lineup: List<LineupItem?>? = null,
)

data class LineupItem(
    val strPlayer: String? = null,
    val strTeam: String? = null,
    val idEvent: String? = null,
    val strEvent: String? = null,
    val intSquadNumber: String? = null,
    val idTeam: String? = null,
    val strFormation: String? = null,
    val idPlayer: String? = null,
    val strPosition: String? = null,
    val strSubstitute: String? = null,
    val strCountry: String? = null,
    val strPositionShort: String? = null,
    val strHome: String? = null,
    val idLineup: String? = null,
    val strSeason: String? = null,
)

data class Lineup(
    var strPlayer: String? = null,
    var intSquadNumber: String? = null,
    var strHome: String? = null,
    var strPlayerHome: String? = null,
    var strFormation: String? = null,
    var strFormationHome: String? = null,
    var strFormationAway: String? = null,
    var intSquadNumberHome: String? = null,
    var strPosition: String? = null,
    var strPlayerAway: String? = null,
    var intSquadNumberAway: String? = null,
)



