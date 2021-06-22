package id.android.soccerapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import id.android.soccerapp.R
import id.android.soccerapp.model.LeaguesItem
import id.android.soccerapp.model.TeamsItem
import id.android.soccerapp.ui.events.EventListViewModel
import id.android.soccerapp.ui.events.rvitem.TeamItem
import id.android.soccerapp.ui.events.rvitem.TeamItemListener
import id.android.soccerapp.ui.events.states.*
import id.android.soccerapp.ui.home.MainActivity
import id.android.soccerapp.ui.team.*
import id.android.soccerapp.ui.teamDetail.TeamDetailActivity
import kotlinx.android.synthetic.main.team_fragment.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class TeamsFragment : DaggerFragment(), TeamItemListener, SearchView.OnQueryTextListener, SearchView.OnCloseListener, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModelTeam: TeamListViewModel

    private lateinit var viewModel: EventListViewModel

    private val groupAdapter = GroupAdapter<ViewHolder>()
    private var isLoading = false
    private var query: String? = ""
    private val ARG_NAME = "arg_name"

    companion object {
        fun newInstance(id: String): TeamsFragment {
            val bundle = Bundle()
            bundle.putString(TeamsFragment().ARG_NAME, id)
            val homeFragment = TeamsFragment()
            homeFragment.arguments = bundle
            return homeFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.team_fragment, container, false)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this) // Providing the dependency
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        (activity as MainActivity).setSupportActionBar(toolbar)
        val toolbar = (activity as MainActivity).supportActionBar
        toolbar?.apply {
            title = "Football"
            elevation = 3F
        }


        viewModelTeam = ViewModelProvider(this, viewModelFactory).get(TeamListViewModel::class.java)
        viewModel = ViewModelProvider(this, viewModelFactory).get(EventListViewModel::class.java)
        setupRv()
        loading.setOnRefreshListener(this)
        observerViewModel()
//        search_toolbar.visibility = View.VISIBLE
//        search_toolbar.setOnQueryTextListener(this)
//        search_toolbar.setOnCloseListener(this)

//        savedInstanceState?.let {
//            viewModel.restoreLeague()
//        } ?: viewModel.updateLeague()

        viewModelTeam.refreshTeamList("UEFA European Championships")

    }

    override fun onQueryTextChange(newText: String?): Boolean {

        return false
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        this.query = query
        viewModelTeam.refreshTeam(query)
        return false
    }

    override fun onClose(): Boolean {
        this.query = ""
//        val selectedLeague = league_spinner.selectedItem as LeaguesItem
        viewModelTeam.refreshTeamList("UEFA European Championships")
        return false
    }

    override fun onAddToCalenderClick(event: TeamsItem?) {

    }

    override fun onItemClick(event: TeamsItem?) {
        val intent = Intent(activity, TeamDetailActivity::class.java)
        intent.putExtra(TeamDetailActivity.TEAM, event)
        startActivity(intent)
    }

    private val stateObserverTeam = Observer<TeamListState> { state ->
        state?.let {

            when (state) {
                is DefaultTeamState -> {
                    isLoading = false
                    loading.isRefreshing = false
                    groupAdapter.clear()
                    // add data to adapter
                    it.data.map {
                        Section().apply {
                            add(TeamItem(context, it, this@TeamsFragment))
                            groupAdapter.add(this)
                        }
                    }
                }
                is LoadingTeamState -> {
                    loading.isRefreshing = true
                    isLoading = true
                }

                is ErrorTeamState -> {
                    Log.e("", "error state")
                }
            }
        }
    }


    private val stateObserverLeague = Observer<LeagueListState> { state ->
        state?.let {
            when (state) {
                is DefaultLeagueState -> {
                    isLoading = false
                    loading.isRefreshing = false
//                    setupSpinner(it.data)
                }
                is LoadingLeagueState -> {
                    loading.isRefreshing = true
                    isLoading = true
                }

                is ErrorLeagueState -> {
                    loading.isRefreshing = false
                    isLoading = false
                }
                is EmptyLeagueState -> TODO()
            }
        }
    }

//    private fun setupSpinner(leagueList: List<LeaguesItem>) {
//        league_spinner.adapter = spinnerAdapter(leagueList)
//        league_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//
//            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
//                val leagueItem = league_spinner.adapter.getItem(position) as LeaguesItem
//                viewModelTeam.refreshTeamList(leagueItem.strLeague)
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {
//
//            }
//        }
//    }

    private fun setupRv() {
        rv_teams.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = groupAdapter
        }
    }

    private fun observerViewModel() {
        viewModelTeam.stateLiveData.observe(viewLifecycleOwner, stateObserverTeam)
        viewModel.stateLeagueLiveData.observe(viewLifecycleOwner, stateObserverLeague)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModelTeam.stateLiveData.removeObserver(stateObserverTeam)
        viewModel.stateLeagueLiveData.removeObserver(stateObserverLeague)
    }

    override fun onRefresh() {
//        val selectedLeague = league_spinner?.selectedItem!! as LeaguesItem
        groupAdapter.clear()

        if (!TextUtils.isEmpty(query)) {
            viewModelTeam.refreshTeam(query)
        } else {
//            if (league_spinner.selectedItem == null)
//                viewModel.updateLeague()
//            else
            viewModelTeam.refreshTeamList("UEFA European Championships")

        }
    }

    private fun spinnerAdapter(leagues: List<LeaguesItem>): ArrayAdapter<LeaguesItem> {
        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, leagues)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return adapter
    }
}
