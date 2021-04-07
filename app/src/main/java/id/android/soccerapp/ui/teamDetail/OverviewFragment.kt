package id.android.soccerapp.ui.teamDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import id.android.soccerapp.R
import id.android.soccerapp.model.TeamsItem
import kotlinx.android.synthetic.main.overview_fragment.*

class OverviewFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.overview_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val parenActivity = activity as TeamDetailActivity
        val teamItem = parenActivity.getDataTeam()
        setupValue(teamItem)
    }

    fun setupValue(teamItem: TeamsItem){
        tv_overview.text = teamItem.strDescriptionEN
    }
}
