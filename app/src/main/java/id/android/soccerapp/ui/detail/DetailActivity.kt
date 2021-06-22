package id.android.soccerapp.ui.detail

import android.content.ContentValues
import android.database.sqlite.SQLiteConstraintException
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import id.android.soccerapp.R
import id.android.soccerapp.data.db.FavoriteEvent
import id.android.soccerapp.data.db.database
import id.android.soccerapp.di.module.GlideApp
import id.android.soccerapp.model.EventsItem
import id.android.soccerapp.model.Lineup
import id.android.soccerapp.ui.events.EventListViewModel
import id.android.soccerapp.ui.events.states.*
import id.android.soccerapp.ui.team.*
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.prev_match_fragment.*
import kotlinx.android.synthetic.main.team_fragment.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.select
import org.jetbrains.anko.toast
import timber.log.Timber
import java.util.*
import javax.inject.Inject


class DetailActivity : DaggerAppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val groupAdapter = GroupAdapter<ViewHolder>()
    private lateinit var viewModelEvent: EventListViewModel
    private lateinit var viewModelTeam: TeamListViewModel
    private lateinit var eventItem: EventsItem

    private var Hometeam: String? = ""
    private var Awayteam: String? = ""
    private var idMatch: String = ""
    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false

    companion object {
        const val ID_MATCH = "id_match"
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val toolbar = findViewById<View>(R.id.toolbar) as androidx.appcompat.widget.Toolbar
        setSupportActionBar(toolbar)
        loading_detail_activity.setOnRefreshListener(this)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        idMatch = intent.getStringExtra(ID_MATCH)

        viewModelEvent =
            ViewModelProvider(this, viewModelFactory).get(EventListViewModel::class.java)
        viewModelTeam = ViewModelProvider(this, viewModelFactory).get(TeamListViewModel::class.java)
        setupRv()
        checkDbIsFav()
        observerViewModel()

        savedInstanceState?.let {
            viewModelEvent.restoreEventDetail()
            viewModelTeam.restoreTeamInfo()
        } ?: viewModelEvent.updateEventDetail(idMatch)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.favorite_menu, menu)
        menuItem = menu
        setFavourite()
        return true
    }

    private fun setFavourite() {
        if (isFavorite)
            menuItem?.getItem(0)?.icon =
                ContextCompat.getDrawable(this, R.drawable.ic_star_black_24dp)
        else
            menuItem?.getItem(0)?.icon =
                ContextCompat.getDrawable(this, R.drawable.ic_star_border_black_24dp)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.fav_menu_toolbar -> {
                processToDb()
                isFavorite = !isFavorite
                setFavourite()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    fun processToDb() {
        val values = ContentValues()
        values.put(FavoriteEvent.EVENT_ID, idMatch)

        values.put(FavoriteEvent.EVENT_DATE, eventItem.dateEvent)
        values.put(FavoriteEvent.HOME_TEAM, eventItem.strHomeTeam)
        values.put(FavoriteEvent.HOME_SCORE, eventItem.intHomeScore)
        values.put(FavoriteEvent.AWAY_TEAM, eventItem.strAwayTeam)
        values.put(FavoriteEvent.AWAY_SCORE, eventItem.intAwayScore)

        if (isFavorite) {
            try {
                this.database.use {
                    delete(
                        FavoriteEvent.TABLE_FAVORITE, "(${FavoriteEvent.EVENT_ID} = {id})",
                        "id" to idMatch
                    )
                    toast(getString(R.string.succes_deleting_record))

                }
            } catch (e: SQLiteConstraintException) {
                toast(getString(R.string.failed_deleting_record))
            }
        } else {
            try {
                this.database.use {
                    insert(FavoriteEvent.TABLE_FAVORITE, null, values)
                    toast(getString(R.string.succes_saving_data))
                }
            } catch (e: SQLiteConstraintException) {
                toast(getString(R.string.failed_to_delete_data))
            }
        }
    }

    fun checkDbIsFav() {
        database.use {
            val result = select(FavoriteEvent.TABLE_FAVORITE)
                .whereArgs(
                    "(${FavoriteEvent.EVENT_ID} = {id})",
                    "id" to idMatch
                )
            val favorite = result.parseList(classParser<FavoriteEvent>())
            if (!favorite.isEmpty()) isFavorite = true
        }
    }

    fun getTeamInfo(id: String?) {
        viewModelTeam.updateTeamInfo(id)
    }

    // state observer
    private val stateObserverEvent = Observer<EventListState> { state ->
        state?.let { it ->
            when (state) {
                is DefaultState -> {
                    loading_detail_activity.isRefreshing = false
                    // add data to adapter
                    it.data.map {
                        Section().apply {
                            setValue(it)
                            GlideApp.with(this@DetailActivity).load(it.strBanner)
                                .into(img_small_banner)
                            eventItem = it
                        }
                    }
                    val positionList: MutableList<String> = mutableListOf()
                    val map = it.dataLineup.associateBy(
                        { it.strPositionShort + it.strHome + it.idPlayer }, {
                            Lineup(
                                strPosition = it.strPosition,
                                strPlayer = it.strPlayer,
                                strHome = it.strHome,
                                intSquadNumber = it.intSquadNumber,
                                strFormation = it.strFormation
                            )
                        })

                    map.forEach { (key, _) ->
                        positionList.add(key)
                    }

                    val result = mutableMapOf<String, MutableList<Lineup>>()
                    map.forEach { (key, value) ->
                        for (pos in positionList) {
                            if (key.contains(pos)) {
                                if (result.containsKey(pos.substring(0, 1))) {
                                    val old = result[pos.substring(0, 1)]?.add(value)
                                    result.plus(pos.substring(0, 1) to old)
                                } else {
                                    result[pos.substring(0, 1)] = mutableListOf(value)
                                }
                                break
                            }
                        }
                    }
                    if (result.isNotEmpty()) groupAdapter.clear()

                    result.forEach { (key, value) ->
                        val lineup = Lineup()
                        for (values in value) {
                            if (values.strHome.equals("Yes")) {
                                lineup.strPlayerHome =
                                    if (lineup.strPlayerHome != null)
                                        lineup.strPlayerHome + values.intSquadNumber + "   " + values.strPlayer + "\n"
                                    else
                                        values.intSquadNumber + "   " + values.strPlayer + "\n"
                                home_team_formation.text = values.strFormation
                            } else {
                                lineup.strPlayerAway =
                                    if (lineup.strPlayerAway != null)
                                        lineup.strPlayerAway + values.strPlayer + "   " + values.intSquadNumber + "\n"
                                    else
                                        values.strPlayer + "   " + values.intSquadNumber + "\n"
                                away_team_formation.text = values.strFormation
                            }
                            lineup.strPosition =
                                when (key.substring(0, 1)) {
                                    "G" -> "Goalkeeper"
                                    "D" -> "Defender"
                                    "M" -> "Midfielder"
                                    "F" -> "Forward"
                                    else -> ""
                                }
                        }
                        groupAdapter.add(id.android.soccerapp.ui.events.rvitem.LineupItem(this@DetailActivity,
                            lineup))
                    }
                }
                is LoadingState -> {
                    println("loading state")
                    loading_detail_activity.isRefreshing = true
                }

                is ErrorState -> {
                    loading_detail_activity.isRefreshing = false
                    Timber.e("error")
                }
                is EmptyState -> {
                    loading_detail_activity.isRefreshing = false
                    toast("No data found")
                }
            }
        }
    }

    private val stateObserverTeam = androidx.lifecycle.Observer<TeamListState> { state ->
        state?.let {

            when (state) {
                is DefaultTeamState -> {
//                    isLoading = false
                    loading_detail_activity.isRefreshing = false
                    // add data to adapter
                    it.data.map {
                        Section().apply {
                            if (it.idTeam.equals(Hometeam)) {
                                GlideApp.with(this@DetailActivity).load(it.strTeamJersey)
                                    .into(img_home_team)
                            } else
                                GlideApp.with(this@DetailActivity).load(it.strTeamJersey)
                                    .into(img_away_team)
                        }
                    }

                }
                is LoadingTeamState -> {
//                    Log.d(TAG, "loading state")
                    loading_detail_activity.isRefreshing = true
//                    isLoading = true
                }

                is ErrorTeamState -> {
                    loading_detail_activity.isRefreshing = false
                    Timber.e("error state")
                }
            }
        }
    }

    private fun observerViewModel() {
        viewModelTeam.stateLiveData.observe(this, stateObserverTeam)
        viewModelEvent.stateLiveData.observe(this, stateObserverEvent)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModelEvent.stateLiveData.removeObserver(stateObserverEvent)
    }

    private fun setupRv() {
        rv_lineup.apply {
            layoutManager = LinearLayoutManager(this@DetailActivity)
            adapter = groupAdapter
        }
    }

    fun setValue(event: EventsItem?) {
//        event_time_detail.text = event?.dateEvent
//        home_team_name.text = event?.strHomeTeam
        home_score.text = event?.intHomeScore
//        home_team_formation.text = event?.strHomeFormation
//        home_team_goal.text = event?.strHomeGoalDetails
//        home_team_shot.text = event?.intHomeShots

//        away_team_name.text = event?.strAwayTeam
        away_score.text = event?.intAwayScore
//        away_team_formation.text = event?.strAwayFormation
//        away_team_goal.text = event?.strAwayGoalDetails
//        away_team_shot.text = event?.intAwayShots

        Hometeam = event?.idHomeTeam
        Awayteam = event?.idAwayTeam

        getTeamInfo(event?.idHomeTeam)
        getTeamInfo(Awayteam)
    }

    override fun onRefresh() {
        viewModelEvent.updateEventDetail(idMatch)
    }

}
