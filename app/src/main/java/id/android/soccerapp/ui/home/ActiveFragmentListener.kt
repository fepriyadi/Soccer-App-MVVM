package id.android.soccerapp.ui.home

interface ActiveFragmentListener{
    fun doOnFragment(query : String?)
    fun doOnClose()
}