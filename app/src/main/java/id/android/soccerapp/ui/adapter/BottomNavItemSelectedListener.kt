package id.android.soccerapp.ui.adapter

import android.view.MenuItem
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.android.soccerapp.R

class BottomNavItemSelectedListener(
    private val viewPager: ViewPager,
) : BottomNavigationView.OnNavigationItemSelectedListener {
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.matches -> {
                viewPager.currentItem = 0
                true
            }
            R.id.teams -> {
                viewPager.currentItem = 1
                true
            }
            R.id.fav_match -> {
                viewPager.currentItem = 2
                true
            }
            else -> false
        }

    }
}