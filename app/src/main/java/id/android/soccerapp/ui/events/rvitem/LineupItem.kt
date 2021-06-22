package id.android.soccerapp.ui.events.rvitem

import android.annotation.SuppressLint
import android.content.Context
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import id.android.soccerapp.R
import id.android.soccerapp.model.Lineup
import kotlinx.android.synthetic.main.lineup_item_adapter.view.*

class LineupItem(private val context: Context?, private val event: Lineup) : Item() {

    @SuppressLint("NewApi")
    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.tv_player.text = event.strPlayerHome?.dropLast(1)
        viewHolder.itemView.tv_player_away.text = event.strPlayerAway?.dropLast(1)
        viewHolder.itemView.tv_player_pos.text = event.strPosition

        viewHolder.itemView.setOnClickListener {

        }
    }

    override fun getLayout(): Int = R.layout.lineup_item_adapter

}