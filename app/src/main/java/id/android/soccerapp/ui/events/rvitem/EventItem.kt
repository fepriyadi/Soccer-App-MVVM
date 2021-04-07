package id.android.soccerapp.ui.events.rvitem

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.os.Build
import android.view.View
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import id.android.soccerapp.R
import id.android.soccerapp.model.EventsItem
import id.android.soccerapp.ui.PrevMatchFragment
import id.android.soccerapp.ui.favourite.FavMatchFragment
import id.android.soccerapp.ui.search.SearchEventActivity
import kotlinx.android.synthetic.main.item_card_view.view.*
import timber.log.Timber.d
import java.text.SimpleDateFormat
import java.util.*

interface EventItemListener {
    fun onItemClick(event: EventsItem?)
    fun onAddToCalenderClick(event: EventsItem?)
}

@TargetApi(Build.VERSION_CODES.O)
class EventItem(private val event: EventsItem,
                private val itemListener: EventItemListener) : Item() {
    var simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    var f  = SimpleDateFormat("HH:mm:ss z")
    var time = SimpleDateFormat("")

    @SuppressLint("NewApi")
    override fun bind(viewHolder: ViewHolder, position: Int) {

        val date : Date = simpleDateFormat.parse(event.dateEvent)

        d("INI TIME ==>> " + event.strTime)

        viewHolder.itemView.event_time.text = SimpleDateFormat("EEEE, dd MMMM yyyy").format(date);
        viewHolder.itemView.first_team_name.text = event.strHomeTeam
        viewHolder.itemView.first_team_score.text = event.intHomeScore
        viewHolder.itemView.second_team_name.text = event.strAwayTeam
        viewHolder.itemView.second_team_score.text = event.intAwayScore
        viewHolder.itemView.tv_time_match.text = event.strTime?.substring(0,5)

        if (itemListener is PrevMatchFragment || itemListener is FavMatchFragment || itemListener is SearchEventActivity){
            viewHolder.itemView.adding.visibility = View.GONE
            viewHolder.itemView.img_add_to_calender.visibility = View.GONE
        }
        else{
            viewHolder.itemView.first_team_score.visibility = View.GONE
            viewHolder.itemView.second_team_score.visibility = View.GONE
        }
        viewHolder.itemView.card_view.setOnClickListener {
            itemListener.onItemClick(event)
        }
        viewHolder.itemView.img_add_to_calender.setOnClickListener{
            itemListener.onAddToCalenderClick(event)
        }
    }

    override fun getLayout(): Int = R.layout.item_card_view

}