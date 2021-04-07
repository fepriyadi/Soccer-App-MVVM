package id.android.soccerapp.ui.events.rvitem

import android.annotation.SuppressLint
import android.content.Context
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import id.android.soccerapp.R
import id.android.soccerapp.di.module.GlideApp
import id.android.soccerapp.model.PlayerItem
import kotlinx.android.synthetic.main.player_item_adapter.view.*

interface PlayerItemListener {
    fun onItemClick(playerItem: PlayerItem)
}

class PlayerItemRV(private val context: Context?, private val playerItem: PlayerItem,
                   private val itemListener: PlayerItemListener) : Item() {

    @SuppressLint("NewApi")
    override fun bind(viewHolder: ViewHolder, position: Int) {
        if (context != null) {
            GlideApp.with(context)
                    .load(playerItem.strCutout)
                    .into(viewHolder.itemView.player_picture)
        }

        viewHolder.itemView.tv_player_name.text = playerItem.strPlayer
        viewHolder.itemView.tv_player_position.text = playerItem.strPosition

        viewHolder.itemView.setOnClickListener {
            itemListener.onItemClick(playerItem)
        }
    }

    override fun getLayout(): Int = R.layout.player_item_adapter

}