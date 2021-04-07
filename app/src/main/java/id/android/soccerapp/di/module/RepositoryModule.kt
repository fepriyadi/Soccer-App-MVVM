package id.android.soccerapp.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import id.android.soccerapp.data.Services
import id.android.soccerapp.data.repository.*

@Module
class RepositoryModule {

    @Provides
    fun providesEventRepo(eventServices: Services,
                          context: Context): EventsRepository = EventsRepositoryImpl(eventServices, context)

    @Provides
    fun providesTeamRepo(eventServices: Services,
                         context: Context): TeamRepository = TeamsRepositoryImpl(eventServices, context)

    @Provides
    fun providesPlayerRepo(eventServices: Services,
                         context: Context): PlayerRepository = PlayerRepositoryImpl(eventServices, context)
}