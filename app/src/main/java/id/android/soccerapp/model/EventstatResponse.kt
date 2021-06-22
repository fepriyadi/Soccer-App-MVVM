package id.android.soccerapp.model

data class EventstatResponse(
    val eventstats: List<EventstatsItem?>? = null,
)

data class EventstatsItem(
    val intHome: String? = null,
    val idStatistic: String? = null,
    val intAway: String? = null,
    val idEvent: String? = null,
    val strStat: String? = null,
    val idApiFootball: String? = null,
    val strEvent: String? = null,
)

