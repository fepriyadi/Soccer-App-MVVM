package id.android.soccerapp.ui.teamDetail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import dagger.android.support.DaggerFragment
import id.android.soccerapp.R
import id.android.soccerapp.model.PlayerItem
import id.android.soccerapp.model.TeamsItem
import id.android.soccerapp.ui.events.rvitem.PlayerItemListener
import id.android.soccerapp.ui.events.rvitem.PlayerItemRV
import id.android.soccerapp.ui.player.*
import kotlinx.android.synthetic.main.player_fragment.*
import javax.inject.Inject

class PlayerFragment : DaggerFragment(), PlayerItemListener, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModelPlayer: PlayerListViewModel
    private val groupAdapter = GroupAdapter<ViewHolder>()
    private var isLoading = false
    private lateinit var teamsItem: TeamsItem

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.player_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val parenActivity = activity as TeamDetailActivity
        teamsItem = parenActivity.getDataTeam()

        viewModelPlayer = ViewModelProvider(this, viewModelFactory).get(PlayerListViewModel::class.java)
        setupRv()
        loading.setOnRefreshListener(this)
        observerViewModel()

        savedInstanceState?.let {
            viewModelPlayer.restorePlayerList()
        } ?: viewModelPlayer.updatePlayerList(teamsItem.idTeam)
    }

    override fun onItemClick(playerItem: PlayerItem) {
        val intent = Intent(activity, PlayerDetailActivity::class.java)
        intent.putExtra(PlayerDetailActivity.PLAYER, playerItem)
        startActivity(intent)
    }

    private val stateObserverPlayer = Observer<PlayerListState> { state ->
        state?.let {
            when (state) {
                is DefaultPlayerState -> {
                    isLoading = false
                    loading.isRefreshing = false
                    groupAdapter.clear()
                    // add data to adapter
                    it.data.map {
                        Section().apply {
                            add(PlayerItemRV(context, it, this@PlayerFragment))
                            groupAdapter.add(this)
                        }
                    }
                }
                is LoadingPlayerState -> {
                    loading.isRefreshing = true
                    isLoading = true
                }

                is ErrorPlayerState -> {
                    Log.e("", "error state")
                }
            }
        }
    }

    private fun setupRv() {
        rv_player.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = groupAdapter
        }
    }

    private fun observerViewModel() {
        viewModelPlayer.stateLiveData.observe(viewLifecycleOwner, stateObserverPlayer)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModelPlayer.stateLiveData.removeObserver(stateObserverPlayer)
    }

    override fun onRefresh() {
        groupAdapter.clear()
        viewModelPlayer.refreshPlayerList(teamsItem.idTeam)
    }
}
