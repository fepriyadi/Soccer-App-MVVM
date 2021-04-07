package id.android.soccerapp.di.component

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import id.android.soccerapp.SoccerApplication
import id.android.soccerapp.di.module.AppModule
import id.android.soccerapp.di.module.NetworkModule
import id.android.soccerapp.di.module.RepositoryModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        AndroidInjectionModule::class,
        AppModule::class,
        NetworkModule::class,
        RepositoryModule::class
))

interface ApplicationComponent : AndroidInjector<SoccerApplication> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: SoccerApplication): Builder

        fun build(): ApplicationComponent
    }
    override fun inject(app: SoccerApplication)
}