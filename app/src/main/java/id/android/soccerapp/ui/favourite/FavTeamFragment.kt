package id.android.soccerapp.ui.favourite


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import dagger.android.support.DaggerFragment
import id.android.soccerapp.R
import id.android.soccerapp.model.TeamsItem
import id.android.soccerapp.ui.events.rvitem.TeamItem
import id.android.soccerapp.ui.events.rvitem.TeamItemListener
import id.android.soccerapp.ui.events.states.*
import id.android.soccerapp.ui.home.MainActivity
import id.android.soccerapp.ui.team.TeamListViewModel
import id.android.soccerapp.ui.teamDetail.TeamDetailActivity
import kotlinx.android.synthetic.main.fragment_fav_team.*
import kotlinx.android.synthetic.main.toolbar.*
import timber.log.Timber
import javax.inject.Inject


class FavTeamFragment : DaggerFragment(), TeamItemListener, SwipeRefreshLayout.OnRefreshListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: TeamListViewModel

    private val groupAdapter = GroupAdapter<ViewHolder>()

    private var isLoading = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_fav_team, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setSupportActionBar(toolbar)
        (activity as MainActivity).supportActionBar?.title = "Your Favorite Team"

        viewModel = ViewModelProvider(this, viewModelFactory).get(TeamListViewModel::class.java)

        setupRv()
        loading.setOnRefreshListener(this)

        observerViewModel()

        savedInstanceState?.let {
            viewModel.restoreFavList()
        } ?: viewModel.updateFavList()

    }

    override fun onItemClick(event: TeamsItem?) {
        val intent = Intent(activity, TeamDetailActivity::class.java)
        intent.putExtra(TeamDetailActivity.TEAM, event)
        startActivity(intent)
    }

    override fun onAddToCalenderClick(event: TeamsItem?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRefresh() {
        groupAdapter.clear()
        viewModel.refreshFavList()
    }

    private fun setupRv() {
        rv_fav_team.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = groupAdapter
        }
    }

    private fun observerViewModel() {
        viewModel.stateFavLiveData.observe(viewLifecycleOwner, stateObserver)
    }

    private val stateObserver = Observer<FavouriteListState> { state ->
        state?.let { it ->
            when (state) {
                is DefaultFavState -> {
                    Timber.d("ini default state")
                    isLoading = false
                    loading.isRefreshing = false

                    it.favTam.map {
                        val team = TeamsItem(
                                idTeam = it.teamId,
                                strTeam = it.teamName,
                                strTeamBadge = it.teamBadge,
                                intFormedYear = it.teamYear,
                                strStadium = it.teamStadium,
                                strDescriptionEN = it.overview
                        )
                        Section().apply {
                            add(TeamItem(context, team, this@FavTeamFragment))
                            groupAdapter.add(this)
                        }
                    }
                }
                is LoadingFavState -> {
                    Timber.d("ini loading state")

                    loading.isRefreshing = true
                    isLoading = true
                }

                is ErrorFavState -> {
                    Timber.d("ini error state")

                    isLoading = false
                    loading.isRefreshing = false
                }
                is EmptyFavState -> {
                    Timber.d("ini empty state")

                    isLoading = false
                    loading.isRefreshing = false
                    Toast.makeText(activity, "Data masih kosong", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stateFavLiveData.removeObserver(stateObserver)
    }
}
