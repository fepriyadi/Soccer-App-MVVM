package id.android.soccerapp.ui.player

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import id.android.soccerapp.R
import id.android.soccerapp.di.module.GlideApp
import id.android.soccerapp.model.PlayerItem
import kotlinx.android.synthetic.main.activity_player_detail.*

class PlayerDetailActivity :DaggerAppCompatActivity() {

    private lateinit var player: PlayerItem

    companion object {
        val PLAYER = "PLAYER"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_detail)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        player = intent.getSerializableExtra(PLAYER) as PlayerItem


        supportActionBar?.title= player.strPlayer
        GlideApp.with(this)
                .load(player.strFanart2)
                .into(player_picture)

        player_position.text = player.strPosition
        weight.text = player.strWeight
        height.text = player.strHeight
        player_des.text = player.strDescriptionEN
    }
}
