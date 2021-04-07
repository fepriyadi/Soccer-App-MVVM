package id.android.soccerapp.di.module
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import id.android.soccerapp.di.DaggerViewModelFactory
import id.android.soccerapp.ui.events.EventListViewModel
import id.android.soccerapp.ui.player.PlayerListViewModel
import id.android.soccerapp.ui.team.TeamListViewModel
import kotlin.reflect.KClass

@MustBeDocumented
@Target(
        AnnotationTarget.FUNCTION,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(EventListViewModel::class)
    abstract fun bindCryptoEventListViewModel(viewModel: EventListViewModel) : ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(TeamListViewModel::class)
    abstract fun bindCryptoTeamListViewModel(viewModel: TeamListViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PlayerListViewModel::class)
    abstract fun bindCryptoPlayerListViewModel(viewModel: PlayerListViewModel) : ViewModel

    /**
     * Binds ViewModels factory to provide ViewModels.
     */
    @Binds
    abstract fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory
}