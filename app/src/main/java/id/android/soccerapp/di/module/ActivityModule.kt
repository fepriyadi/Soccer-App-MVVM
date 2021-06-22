package id.android.soccerapp.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import id.android.soccerapp.ui.detail.DetailActivity
import id.android.soccerapp.ui.home.MainActivity
import id.android.soccerapp.ui.player.PlayerDetailActivity
import id.android.soccerapp.ui.search.SearchEventActivity
import id.android.soccerapp.ui.teamDetail.TeamDetailActivity

/**
 * All your Activities participating in DI would be listed here.
 */
@Module(includes = [FragmentModule::class]) // Including Fragment Module Available For Activities
abstract class ActivityModule {

    /**
     * Marking Activities to be available to contributes for Android Injector
     */
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeDetailActivity(): DetailActivity

    @ContributesAndroidInjector
    abstract fun contributeSearchEventActivity(): SearchEventActivity

    @ContributesAndroidInjector
    abstract fun contributeTeamDetailActivity(): TeamDetailActivity

    @ContributesAndroidInjector
    abstract fun contributePlayerDetailActivity(): PlayerDetailActivity
}
