package id.android.soccerapp.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import id.android.soccerapp.ui.MatchFragment
import id.android.soccerapp.ui.PrevMatchFragment
import id.android.soccerapp.ui.TeamsFragment
import id.android.soccerapp.ui.UpComingMatchFragment
import id.android.soccerapp.ui.favourite.FavFragment
import id.android.soccerapp.ui.favourite.FavMatchFragment
import id.android.soccerapp.ui.favourite.FavTeamFragment
import id.android.soccerapp.ui.teamDetail.PlayerFragment

@Suppress("unused")
@Module
abstract class FragmentModule {

    /**
     * Injecting Fragments
     */
    @ContributesAndroidInjector
    internal abstract fun contributeMatchFragment(): MatchFragment
    @ContributesAndroidInjector
    internal abstract fun contributeTeamFragment(): TeamsFragment
    @ContributesAndroidInjector
    internal abstract fun contributePrevMatchFragment(): PrevMatchFragment
    @ContributesAndroidInjector
    internal abstract fun contributeUpcomingMatchFragment(): UpComingMatchFragment
    @ContributesAndroidInjector
    internal abstract fun contributeFavMatchFragment(): FavMatchFragment
    @ContributesAndroidInjector
    internal abstract fun contributeFavTeamFragment(): FavTeamFragment
    @ContributesAndroidInjector
    internal abstract fun contributePlayerFragment(): PlayerFragment
    @ContributesAndroidInjector
    internal abstract fun contributeFavFragment(): FavFragment



}