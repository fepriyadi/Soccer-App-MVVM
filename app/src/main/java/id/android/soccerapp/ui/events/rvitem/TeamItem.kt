package id.android.soccerapp.ui.events.rvitem

import android.annotation.SuppressLint
import android.content.Context
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import id.android.soccerapp.R
import id.android.soccerapp.di.module.GlideApp
import id.android.soccerapp.model.TeamsItem
import kotlinx.android.synthetic.main.team_item_adapter.view.*

interface TeamItemListener {
    fun onItemClick(event: TeamsItem?)
    fun onAddToCalenderClick(event: TeamsItem?)
}

class TeamItem(private val context: Context?, private val event: TeamsItem,
                private val itemListener: TeamItemListener) : Item() {

    @SuppressLint("NewApi")
    override fun bind(viewHolder: ViewHolder, position: Int) {
        if (context != null) {
            GlideApp.with(context)
                .load(event.strTeamBadge)
                .into(viewHolder.itemView.team_logo)

            GlideApp.with(context)
                .load(event.strTeamJersey)
                .into(viewHolder.itemView.team_jersey)
        }

        viewHolder.itemView.tv_team_name.text = event.strTeam

        viewHolder.itemView.setOnClickListener {
            itemListener.onItemClick(event)
        }
    }

    override fun getLayout(): Int = R.layout.team_item_adapter

}