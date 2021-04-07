package id.android.soccerapp.ui.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import id.android.soccerapp.R
import id.android.soccerapp.ui.PrevMatchFragment
import id.android.soccerapp.ui.UpComingMatchFragment
import java.util.*

class MatchViewPagerAdapter(private val mContext: Context?, fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val fragments = ArrayList<Fragment>()

    init {
        fragments.add(UpComingMatchFragment())
        fragments.add(PrevMatchFragment())
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            1 -> return mContext?.getString(R.string.prev_match)
            0 -> return mContext?.getString(R.string.next_match)
        }
        return null
    }
}