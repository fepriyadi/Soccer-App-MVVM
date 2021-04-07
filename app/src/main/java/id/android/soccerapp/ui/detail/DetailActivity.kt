package id.android.soccerapp.ui.detail

import android.content.ContentValues
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.xwray.groupie.Section
import dagger.android.support.DaggerAppCompatActivity
import id.android.soccerapp.R
import id.android.soccerapp.data.db.FavoriteEvent
import id.android.soccerapp.data.db.database
import id.android.soccerapp.di.module.GlideApp
import id.android.soccerapp.model.EventsItem
import id.android.soccerapp.ui.events.EventListViewModel
import id.android.soccerapp.ui.events.states.*
import id.android.soccerapp.ui.team.*
import kotlinx.android.synthetic.main.activity_detail.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.select
import org.jetbrains.anko.toast
import javax.inject.Inject


class DetailActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    private lateinit var viewModelEvent: EventListViewModel
    private lateinit var viewModelTeam: TeamListViewModel
    private lateinit var eventItem: EventsItem

    var Hometeam: String? = ""
    var Awayteam: String? = ""
    var idMatch: String = ""
    private var menuItem: Menu? = null
    var isFavorite: Boolean = false

    companion object {
        val ID_MATCH = "id_match"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
//        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        idMatch = intent.getStringExtra(ID_MATCH)

        viewModelEvent = ViewModelProvider(this, viewModelFactory).get(EventListViewModel::class.java)
        viewModelTeam = ViewModelProvider(this, viewModelFactory).get(TeamListViewModel::class.java)

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
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_star_black_24dp)
        else
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_star_border_black_24dp)
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
                    delete(FavoriteEvent.TABLE_FAVORITE, "(${FavoriteEvent.EVENT_ID} = {id})",
                            "id" to idMatch)
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
                    .whereArgs("(${FavoriteEvent.EVENT_ID} = {id})",
                            "id" to idMatch)
            val favorite = result.parseList(classParser<FavoriteEvent>())
            if (!favorite.isEmpty()) isFavorite = true
        }
    }

    fun getTeamInfo(id: String?) {
        viewModelTeam.updateTeamInfo(id)
    }

    // state observer
    private val stateObserverEvent = Observer<EventListState> { state ->
        state?.let {
            when (state) {
                is DefaultState -> {
//                    isLoading = false
//                    loading.isRefreshing = false

                    // add data to adapter
                    it.data.map {
                        Section().apply {
                            setValue(it)
                            eventItem = it
                        }
                    }
                }
                is LoadingState -> {
//                    Log.d(TAG, "loading state")
//                    loading.isRefreshing = true
//                    isLoading = true
                }

                is ErrorState -> {
                    Log.e("", "error state")
                }
                is EmptyState -> TODO()
            }
        }
    }

    private val stateObserverTeam = Observer<TeamListState> { state ->
        state?.let {

            when (state) {
                is DefaultTeamState -> {
//                    isLoading = false
//                    loading.isRefreshing = false
                    // add data to adapter
                    it.data.map {
                        Section().apply {
                            if (it.idTeam.equals(Hometeam)) {
                                GlideApp.with(this@DetailActivity).load(it.strTeamBadge).into(img_home_team)
                            } else
                                GlideApp.with(this@DetailActivity).load(it.strTeamBadge).into(img_away_team)

                        }
                    }

                }
                is LoadingTeamState -> {
//                    Log.d(TAG, "loading state")
//                    loading.isRefreshing = true
//                    isLoading = true
                }

                is ErrorTeamState -> {
                    Log.e("", "error state")
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

    fun setValue(event: EventsItem?) {
        event_time_detail.text = event?.dateEvent
        home_team_name.text = event?.strHomeTeam
        home_score.text = event?.intHomeScore
        home_team_formation.text = event?.strHomeFormation
        home_team_goal.text = event?.strHomeGoalDetails
        home_team_shot.text = event?.intHomeShots
        gk_home_team.text = event?.strHomeLineupGoalkeeper
        df_home_team.text = event?.strHomeLineupDefense
        md_home_team.text = event?.strHomeLineupMidfield
        fw_home_team.text = event?.strHomeLineupForward
        sub_home_team.text = event?.strHomeLineupSubstitutes

        away_team_name.text = event?.strAwayTeam
        away_score.text = event?.intAwayScore
        away_team_formation.text = event?.strAwayFormation
        away_team_goal.text = event?.strAwayGoalDetails
        away_team_shot.text = event?.intAwayShots
        gk_away_team.text = event?.strAwayLineupGoalkeeper
        df_away_team.text = event?.strAwayLineupDefense
        md_away_team.text = event?.strAwayLineupMidfield
        fw_away_team.text = event?.strAwayLineupForward
        sub_away_team.text = event?.strAwayLineupSubstitutes

        Hometeam = event?.idHomeTeam
        Awayteam = event?.idAwayTeam

        getTeamInfo(event?.idHomeTeam)
        getTeamInfo(Awayteam)
    }
}
