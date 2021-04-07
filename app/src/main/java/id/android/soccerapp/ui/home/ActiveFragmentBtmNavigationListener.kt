package id.android.soccerapp.ui.home

import androidx.fragment.app.Fragment

interface ActiveFragmentBtmNavigationListener{
    fun OnQueryChanged(query : String?)
    fun onClose()
    fun getFragment() : Fragment
}