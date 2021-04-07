package id.android.soccerapp.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
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
import id.android.soccerapp.model.EventsItem
import id.android.soccerapp.model.LeaguesItem
import id.android.soccerapp.ui.detail.DetailActivity
import id.android.soccerapp.ui.events.EventListViewModel
import id.android.soccerapp.ui.events.rvitem.EventItem
import id.android.soccerapp.ui.events.rvitem.EventItemListener
import id.android.soccerapp.ui.events.states.*
import id.android.soccerapp.ui.home.ActiveFragmentListener
import id.android.soccerapp.ui.home.ActiveTabListener
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.upcoming_match_fragment.*
import javax.inject.Inject

class UpComingMatchFragment : DaggerFragment(), EventItemListener, SwipeRefreshLayout.OnRefreshListener,
    ActiveFragmentListener,
    ActiveTabListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: EventListViewModel

    private val groupAdapter = GroupAdapter<ViewHolder>()
    private var isLoading = false

    // state observer
    private val stateObserver = Observer<EventListState> { state ->
        state?.let {
            when (state) {
                is DefaultState -> {
                    isLoading = false
                    loading.isRefreshing = false
                    groupAdapter.clear()
                    // add data to adapter
                    it.data.map {
                        Section().apply {
                            add(EventItem(it, this@UpComingMatchFragment))
                            groupAdapter.add(this)
                        }
                    }

                }
                is LoadingState -> {
                    loading.isRefreshing = true
                    isLoading = true
                }

                is ErrorState -> {
                }
                is EmptyState -> TODO()
            }
        }
    }
    private val stateObserverLeague = Observer<LeagueListState> { state ->
        state?.let {
            when (state) {
                is DefaultLeagueState -> {
                    isLoading = false
                    loading.isRefreshing = false
                    setupSpinner(it.data)
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

    override fun getFragment(): Fragment {
        return this
    }

    private fun setupSpinner(leagueList: List<LeaguesItem>) {
        league_spinner.adapter = spinnerAdapter(leagueList)
        league_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val leagueItem = league_spinner.adapter.getItem(position) as LeaguesItem
                viewModel.updateNextMatch(leagueItem.idLeague)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    override fun onAddToCalenderClick(event: EventsItem?) {

    }

    override fun doOnClose() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.upcoming_match_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory).get(EventListViewModel::class.java)

        setupRv()
        loading.setOnRefreshListener(this)
        observerViewModel()

        val parentFrag = this@UpComingMatchFragment.parentFragment as MatchFragment
        parentFrag.setActiveTab(this)

        savedInstanceState?.let {
            viewModel.restoreLeague()
        } ?: viewModel.updateLeague()

        val elevation = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity?.toolbar?.elevation = elevation.toFloat()
        }
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this) // Providing the dependency
        super.onAttach(context)
    }

    override fun doOnFragment(query: String?) {

    }

    private fun setupRv() {
        rv_upcoming_match.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = groupAdapter
        }
    }

    private fun observerViewModel() {
        viewModel.stateLiveData.observe(viewLifecycleOwner, stateObserver)
        viewModel.stateLeagueLiveData.observe(viewLifecycleOwner, stateObserverLeague)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stateLiveData.removeObserver(stateObserver)
    }

    override fun onItemClick(event: EventsItem?) {
        val intent = Intent(activity, DetailActivity::class.java)
        intent.putExtra(DetailActivity.ID_MATCH, event?.idEvent)
        startActivity(intent)
    }

    override fun onRefresh() {
        groupAdapter.clear()
        val selectedLeague = league_spinner?.selectedItem!! as LeaguesItem
        if (league_spinner != null && league_spinner.selectedItem == null)
            viewModel.updateLeague()
        else
            viewModel.refreshEventNextMatch(selectedLeague.idLeague)
    }

    private fun spinnerAdapter(leagues: List<LeaguesItem>): ArrayAdapter<LeaguesItem> {
        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, leagues)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return adapter
    }

}
