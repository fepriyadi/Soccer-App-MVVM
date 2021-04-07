package id.android.soccerapp.ui.search

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import id.android.soccerapp.R
import id.android.soccerapp.model.EventsItem
import id.android.soccerapp.ui.detail.DetailActivity
import id.android.soccerapp.ui.events.EventListViewModel
import id.android.soccerapp.ui.events.rvitem.EventItem
import id.android.soccerapp.ui.events.rvitem.EventItemListener
import id.android.soccerapp.ui.events.states.*
import kotlinx.android.synthetic.main.activity_search_event.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class SearchEventActivity : DaggerAppCompatActivity(), EventItemListener, SearchView.OnQueryTextListener, SearchView.OnCloseListener, SwipeRefreshLayout.OnRefreshListener{
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: EventListViewModel

    private val groupAdapter = GroupAdapter<ViewHolder>()

    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_event)

        viewModel = ViewModelProvider(this, viewModelFactory).get(EventListViewModel::class.java)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val elevation = 5;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            supportActionBar?.elevation = elevation.toFloat()
        }
        search_toolbar.visibility = View.VISIBLE
        search_toolbar.setOnQueryTextListener(this)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        setupRv()
        loading.setOnRefreshListener(this)

        observerViewModel()
    }

    override fun onAddToCalenderClick(event: EventsItem?) {

    }
    override fun onItemClick(event: EventsItem?) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.ID_MATCH, event?.idEvent)
        startActivity(intent)
    }

    private val stateObserver = Observer<EventListState> { state ->
        state?.let {
            when (state) {
                is DefaultState -> {

                    isLoading = false
                    loading.isRefreshing = false

                    groupAdapter.clear()
                    it.data.map {
                        Section().apply {
                            add(EventItem(it, this@SearchEventActivity))
                            groupAdapter.add(this)
                        }
                    }
                }
                is LoadingState -> {
                    loading.isRefreshing = true
                    isLoading = true

                }

                is ErrorState -> {
                    loading.isRefreshing = false
                    isLoading = false


                }
                is EmptyState -> {
                    Snackbar.make(root,"Hasil Pencarian tidak ditemukan", Snackbar.LENGTH_INDEFINITE)
                }

            }
        }
    }

    private fun setupRv() {
        rv_search_match.apply {
            layoutManager = LinearLayoutManager(this@SearchEventActivity)
            adapter = groupAdapter
        }
    }

    private fun observerViewModel() {
        viewModel.stateLiveData.observe(this, stateObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stateLiveData.removeObserver(stateObserver)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        search_toolbar.isIconified = false
        search_toolbar.requestFocusFromTouch()
        return super.onCreateOptionsMenu(menu)
    }

    override fun onClose(): Boolean {
        groupAdapter.clear()
        return false
    }

    override fun onRefresh() {
        groupAdapter.clear()
        val name = search_toolbar.query.toString().replace(" " , "_")
        viewModel.refreshSearchEvent(name)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        val name : String = newText!!.replace(" ", "_")
        viewModel.updateSearchMatch(name)
        return false
    }

}
