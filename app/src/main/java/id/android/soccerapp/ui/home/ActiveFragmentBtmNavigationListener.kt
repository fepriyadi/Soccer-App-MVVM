package id.android.soccerapp.ui.home

interface ActiveFragmentBtmNavigationListener{
    fun onQueryChanged(query: String?)
    fun onCloseActiveFragment()
}