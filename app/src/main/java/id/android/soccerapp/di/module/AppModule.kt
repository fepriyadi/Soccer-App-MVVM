package id.android.soccerapp.di.module
import android.content.Context
import dagger.Module
import dagger.Provides
import id.android.soccerapp.SoccerApplication
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Named
import javax.inject.Singleton

const val SCHEDULER_MAIN_THREAD = "mainThread"
const val SCHEDULER_IO = "io"

@Module(includes = [ViewModelModule::class, ActivityModule::class])
class AppModule {

    @Provides
    @Singleton
    fun provideContext(app: SoccerApplication): Context = app.applicationContext

    @Provides
    @Named(SCHEDULER_MAIN_THREAD)
    fun provideAndroidMainThreadScheduler() : Scheduler = AndroidSchedulers.mainThread()

    @Provides
    @Named(SCHEDULER_IO)
    fun provideIoScheduler() : Scheduler = Schedulers.io()
    }