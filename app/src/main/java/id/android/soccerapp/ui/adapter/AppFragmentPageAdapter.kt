package id.android.soccerapp.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import id.android.soccerapp.ui.MatchFragment
import id.android.soccerapp.ui.TeamsFragment
import id.android.soccerapp.ui.favourite.FavFragment

internal class AppFragmentPageAdapter(fm: FragmentManager?) : FragmentPagerAdapter(
    fm!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                MatchFragment.newInstance(1)
            }
            1 -> {
                TeamsFragment.newInstance("teams")
            }
            2 -> {
                FavFragment.newInstance()
            }
            else -> throw RuntimeException("Not supported")
        }
    }

    override fun getCount(): Int {
        return 3
    }
}