package id.android.soccerapp.ui.home

import android.os.Bundle
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import id.android.soccerapp.R
import id.android.soccerapp.ui.adapter.AppFragmentPageAdapter
import id.android.soccerapp.ui.adapter.BottomNavItemSelectedListener
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : DaggerAppCompatActivity() {

    private var activeFragment: ActiveFragmentListener? = null
    private lateinit var activeTab: ActiveTabListener
    private var activeFragmentBtmNavigationListener: ActiveFragmentBtmNavigationListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar_main)
//        setActiveFrament(this)
        val adapter = AppFragmentPageAdapter(supportFragmentManager)
        custom_view_pager.adapter = adapter
        custom_view_pager.offscreenPageLimit = adapter.count - 1
        val listener = BottomNavItemSelectedListener(custom_view_pager)
        navigationView.setOnNavigationItemSelectedListener(listener)
//        toolbar.post { toolbar.title = navigationView.menu.getItem(0).title }

//        navigationView.setOnNavigationItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.prev_match -> {
//                    loadMatchFragment(savedInstanceState)
//                }
//                R.id.up_coming_match -> {
//                    loadTeamsFragment(savedInstanceState)
//                }
//
//                R.id.fav_match -> {
//                    loadFavMatch(savedInstanceState)
//                }
//            }
//            true
//        }
        println("SELECTED ====> " + navigationView.selectedItemId)
        navigationView.selectedItemId = R.id.matches
    }

//    override fun onQueryTextSubmit(query: String?): Boolean {
//        activeFragmentBtmNavigationListener?.onQueryChanged(query)
//        return false
//    }
//
//    override fun onQueryTextChange(newText: String?): Boolean {
//
//        return false
//    }

//    private fun loadMatchFragment(savedInstanceState: Bundle?) {
//        if (savedInstanceState == null) {
//            supportFragmentManager
//                .beginTransaction()
//                .replace(R.id.main_container,
//                    MatchFragment.newInstance(),
//                    MatchFragment::class.simpleName)
//                .commit()
//        }
//    }
//
//
//    private fun loadTeamsFragment(savedInstanceState: Bundle?) {
//        if (savedInstanceState == null) {
//            supportFragmentManager
//                .beginTransaction()
//                .replace(R.id.main_container,
//                    TeamsFragment.newInstance(),
//                    TeamsFragment::class.simpleName)
//                .commit()
//        }
//    }
//
//    private fun loadFavMatch(savedInstanceState: Bundle?) {
//        if (savedInstanceState == null) {
////            search_toolbar.visibility = View.GONE
//            supportFragmentManager
//                .beginTransaction()
//                .replace(R.id.main_container,
//                    FavFragment.newInstance(),
//                    FavFragment::class.simpleName)
//                .commit()
//        }
//    }

    fun setActiveFrament(activeFragmentBtmNavigationListener: ActiveFragmentBtmNavigationListener) {
        this.activeFragmentBtmNavigationListener = activeFragmentBtmNavigationListener
    }


//    override fun onQueryChanged(query: String?) {
//        activeFragment?.doOnFragment(query)
//    }
//
//    override fun onCloseActiveFragment() {
//        activeFragment?.doOnClose()
//    }
//
//    override fun onClose(): Boolean {
//        activeFragmentBtmNavigationListener?.onCloseActiveFragment()
//        return false
//    }

    fun setActiveTab(activeTabListener: ActiveTabListener) {
        this.activeTab = activeTabListener
    }

    //////////////////
//    override fun onPageSelected(position: Int) {
//        activeFragment = matchViewPagerAdapter.getItem(position) as ActiveFragmentListener
//    }
//
//    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//
//    }
//
//    override fun onPageScrollStateChanged(state: Int) {

//    }

}
