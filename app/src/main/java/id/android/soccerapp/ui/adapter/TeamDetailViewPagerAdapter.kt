package id.android.soccerapp.ui.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import id.android.soccerapp.R
import id.android.soccerapp.ui.teamDetail.OverviewFragment
import id.android.soccerapp.ui.teamDetail.PlayerFragment
import java.util.*

class TeamDetailViewPagerAdapter(private val mContext: Context?, fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val fragments = ArrayList<Fragment>()

    init {
        fragments.add(OverviewFragment())
        fragments.add(PlayerFragment())
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return mContext?.getString(R.string.overview)
            1 -> return mContext?.getString(R.string.players)
        }
        return null
    }
}