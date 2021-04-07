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
import id.android.soccerapp.model.EventsItem
import id.android.soccerapp.ui.detail.DetailActivity
import id.android.soccerapp.ui.events.EventListViewModel
import id.android.soccerapp.ui.events.rvitem.EventItem
import id.android.soccerapp.ui.events.rvitem.EventItemListener
import id.android.soccerapp.ui.events.states.*
import kotlinx.android.synthetic.main.fragment_fav_match.*
import kotlinx.android.synthetic.main.toolbar.*
import id.android.soccerapp.ui.home.MainActivity
import timber.log.Timber
import javax.inject.Inject

class FavMatchFragment : DaggerFragment(), EventItemListener, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: EventListViewModel

    private val groupAdapter = GroupAdapter<ViewHolder>()
    private var isLoading = false
    companion object {
        fun newInstance() = FavFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fav_match, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setSupportActionBar(toolbar)
        (activity as MainActivity).supportActionBar?.title = "Your Favorite Match"
        viewModel = ViewModelProvider(this, viewModelFactory).get(EventListViewModel::class.java)

        setupRv()
        loading.setOnRefreshListener(this)

        observerViewModel()

        savedInstanceState?.let {
            viewModel.restoreFavList()
        } ?: viewModel.updateFavList()

    }

    override fun onAddToCalenderClick(event: EventsItem?) {
    }

    override fun onRefresh() {
        groupAdapter.clear()
        viewModel.refreshFavList()
    }

    override fun onItemClick(event: EventsItem?) {
        val intent = Intent(activity, DetailActivity::class.java)
        intent.putExtra(DetailActivity.ID_MATCH, event?.idEvent)
        startActivity(intent)
    }

    private fun setupRv() {
        rv_fav_match.apply {
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

                    it.data.map {
                        val event = EventsItem(
                                idEvent = it.eventId,
                                dateEvent = it.evenDate,
                                strHomeTeam = it.homeTeam,
                                strAwayTeam = it.awayTeam,
                                intHomeScore = it.homeScore,
                                intAwayScore = it.awayScore
                        )
                        Section().apply {
                            add(EventItem(event, this@FavMatchFragment))
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
