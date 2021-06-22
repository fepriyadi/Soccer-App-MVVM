package id.android.soccerapp.ui.favourite

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import id.android.soccerapp.R
import id.android.soccerapp.ui.adapter.FavViewPagerAdapter
import kotlinx.android.synthetic.main.fav_fragment.*

/**
 * Created by developer on 9/24/18.
 */
class FavFragment : DaggerFragment() {
    private lateinit var favViewPagerAdapter: FavViewPagerAdapter

    companion object {
        fun newInstance() = FavFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fav_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        (activity as MainActivity).setSupportActionBar(toolbar)

        favViewPagerAdapter = FavViewPagerAdapter(context, childFragmentManager)
        view_pager.adapter = favViewPagerAdapter
        tabs.setupWithViewPager(view_pager)

    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this) // Providing the dependency
        super.onAttach(context)
    }

}