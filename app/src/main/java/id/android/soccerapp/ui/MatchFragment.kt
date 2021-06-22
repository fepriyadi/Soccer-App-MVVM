package id.android.soccerapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import id.android.soccerapp.R
import id.android.soccerapp.ui.adapter.MatchViewPagerAdapter
import id.android.soccerapp.ui.home.ActiveFragmentBtmNavigationListener
import id.android.soccerapp.ui.home.ActiveFragmentListener
import id.android.soccerapp.ui.home.ActiveTabListener
import id.android.soccerapp.ui.home.MainActivity
import id.android.soccerapp.ui.search.SearchEventActivity
import kotlinx.android.synthetic.main.match_fragment.*


class MatchFragment : DaggerFragment(), ViewPager.OnPageChangeListener, ActiveTabListener,
    ActiveFragmentBtmNavigationListener {

    private var activeFragment: ActiveFragmentListener? = null
    private lateinit var matchViewPagerAdapter: MatchViewPagerAdapter
    private lateinit var activeTab: ActiveTabListener
    private val ARG_ID = "arg_id"

    companion object {
        fun newInstance(id: Int): MatchFragment {
            val bundle = Bundle()
            bundle.putInt(MatchFragment().ARG_ID, id)
            val homeFragment = MatchFragment()
            homeFragment.arguments = bundle
            return homeFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.match_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        (activity as MainActivity).setActiveFrament(this)
//        (activity as MainActivity).setSupportActionBar(toolbar_main)
        (activity as MainActivity).supportActionBar?.title = "Football App"

        matchViewPagerAdapter = MatchViewPagerAdapter(context, childFragmentManager)
        view_pager.adapter = matchViewPagerAdapter
        view_pager.addOnPageChangeListener(this)
        tabs.setupWithViewPager(view_pager)

    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this) // Providing the dependency
        super.onAttach(context)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search_menu_toolbar -> {
                startActivity(Intent(activity, SearchEventActivity::class.java))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun getFragment(): Fragment {
        return this
    }

    override fun onPageSelected(position: Int) {
        activeFragment = matchViewPagerAdapter.getItem(position) as ActiveFragmentListener
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    fun setActiveTab(activeTabListener: ActiveTabListener) {
        this.activeTab = activeTabListener
    }

    override fun onQueryChanged(query: String?) {
        activeFragment?.doOnFragment(query)
    }

    override fun onCloseActiveFragment() {
        TODO("Not yet implemented")
    }
}