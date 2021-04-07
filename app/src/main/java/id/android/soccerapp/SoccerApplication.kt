package id.android.soccerapp

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import id.android.soccerapp.di.component.DaggerApplicationComponent

class SoccerApplication : DaggerApplication() {

    private val applicationInjector = DaggerApplicationComponent.builder().application(this).build()

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> = applicationInjector

}