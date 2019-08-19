package com.example.getappengine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.login_fragment.view.*
import kotlinx.android.synthetic.main.options_fragment.view.*

class OptionsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.options_fragment, container, false)
        // Set an error if the password is less than 8 characters.


        view.see_all_events_button.setOnClickListener({
            (activity as NavigationHost).navigateTo(ListingFragment(), false)
        })

        view.search_events_date_button.setOnClickListener({
            (activity as NavigationHost).navigateTo(Events_Date(), false)
        })

        view.search_events_venue_button.setOnClickListener({
            (activity as NavigationHost).navigateTo(Events_venue(), false)
        })

        view.see_your_reservations_button.setOnClickListener({
            (activity as NavigationHost).navigateTo(ViewReservations(), false)
        })

        view.logout.setOnClickListener({
            (activity as NavigationHost).navigateTo(LogoutFragment(), false)
        })
        return view
    }
}