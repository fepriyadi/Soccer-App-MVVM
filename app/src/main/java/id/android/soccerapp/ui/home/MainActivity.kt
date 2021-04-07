package id.android.soccerapp.ui.home

import android.os.Bundle
import androidx.appcompat.widget.SearchView
import dagger.android.support.DaggerAppCompatActivity
import id.android.soccerapp.R
import id.android.soccerapp.ui.MatchFragment
import id.android.soccerapp.ui.TeamsFragment
import id.android.soccerapp.ui.favourite.FavFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : DaggerAppCompatActivity(), SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    private var activeFragmentBtmNavigationListener : ActiveFragmentBtmNavigationListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.prev_match -> {
                    loadMatchFragment(savedInstanceState)
                }
                R.id.up_coming_match -> {
                    loadTeamsFragment(savedInstanceState)
                }

                R.id.fav_match -> {
                    loadFavMatch(savedInstanceState)
                }
            }
            true
        }
        navigationView.selectedItemId = R.id.prev_match
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        activeFragmentBtmNavigationListener?.OnQueryChanged(query)
        return false
    }

    override fun onClose(): Boolean {
        activeFragmentBtmNavigationListener?.onClose()
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {

        return false
    }

    private fun loadMatchFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_container, MatchFragment.newInstance(), MatchFragment::class.simpleName)
                .commit()
        }
    }


    private fun loadTeamsFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_container, TeamsFragment.newInstance(), TeamsFragment::class.simpleName)
                .commit()
        }
    }

    private fun loadFavMatch(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
//            search_toolbar.visibility = View.GONE
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_container, FavFragment.newInstance(), FavFragment::class.simpleName)
                .commit()
        }
    }

    fun setActiveFrament(activeFragmentBtmNavigationListener: ActiveFragmentBtmNavigationListener){
        this.activeFragmentBtmNavigationListener = activeFragmentBtmNavigationListener
    }

}
