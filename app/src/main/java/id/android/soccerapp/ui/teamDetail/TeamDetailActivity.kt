package id.android.soccerapp.ui.teamDetail

import android.content.ContentValues
import android.database.sqlite.SQLiteConstraintException
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import dagger.android.support.DaggerAppCompatActivity
import id.android.soccerapp.R
import id.android.soccerapp.data.db.FavoriteTeam
import id.android.soccerapp.data.db.database
import id.android.soccerapp.di.module.GlideApp
import id.android.soccerapp.model.TeamsItem
import id.android.soccerapp.ui.adapter.TeamDetailViewPagerAdapter
import kotlinx.android.synthetic.main.activity_team_detail.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.select
import org.jetbrains.anko.toast

class TeamDetailActivity : DaggerAppCompatActivity() {

    private lateinit var teamItem: TeamsItem
    private lateinit var teamDetailViewPagerAdapter: TeamDetailViewPagerAdapter
    private var menuItem: Menu? = null
    var isFavorite: Boolean = false
    var teamId: String = ""

    companion object {
        val TEAM = "TEAM"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_detail)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        val elevation = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation = elevation.toFloat()
        }

        teamDetailViewPagerAdapter = TeamDetailViewPagerAdapter(this, supportFragmentManager)
        view_pager.adapter = teamDetailViewPagerAdapter
        tabs.setupWithViewPager(view_pager)
        teamItem = intent.getSerializableExtra(TEAM) as TeamsItem
        teamId = teamItem.idTeam!!
        checkDbIsFav()
        GlideApp.with(this)
                .load(teamItem.strTeamBadge)
                .into(img_team_badge)
        tv_team_name.text = teamItem.strTeam
        tv_team_year.text = teamItem.intFormedYear
        tv_team_stadium.text = teamItem.strStadium

    }

    fun getDataTeam(): TeamsItem {
        return teamItem
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
        values.put(FavoriteTeam.TEAM_ID, teamId)
        values.put(FavoriteTeam.TEAM_NAME, teamItem.strTeam)
        values.put(FavoriteTeam.TEAM_BADGE, teamItem.strTeamBadge)
        values.put(FavoriteTeam.TEAM_YEAR, teamItem.intFormedYear)
        values.put(FavoriteTeam.TEAM_STADIUM, teamItem.strStadium)
        values.put(FavoriteTeam.TEAM_OVERVIEW, teamItem.strDescriptionEN)

        if (isFavorite) {
            try {
                this.database.use {
                                        delete(FavoriteTeam.TABLE_FAVORITE, "(${FavoriteTeam.TEAM_ID} = {id})",
                                                "id" to teamId)
                    toast(getString(R.string.succes_deleting_record))

                }
            } catch (e: SQLiteConstraintException) {
                toast(getString(R.string.failed_deleting_record))
            }
        } else {
            try {
                this.database.use {
                    insert(FavoriteTeam.TABLE_FAVORITE, null, values)
                    toast(getString(R.string.succes_saving_data))
                }
            } catch (e: SQLiteConstraintException) {
                toast(getString(R.string.failed_to_delete_data))
            }
        }
    }

    fun checkDbIsFav() {
        database.use {
            val result = select(FavoriteTeam.TABLE_FAVORITE)
                    .whereArgs("(${FavoriteTeam.TEAM_ID} = {id})",
                            "id" to teamId)
            val favorite = result.parseList(classParser<FavoriteTeam>())
            if (!favorite.isEmpty()) isFavorite = true
        }
    }

}