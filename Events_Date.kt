package com.example.getappengine

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import kotlinx.android.synthetic.main.events_date_fragment.*
import kotlinx.android.synthetic.main.events_date_fragment.view.*
import kotlinx.android.synthetic.main.events_date_fragment.view.go_back_button

import kotlinx.android.synthetic.main.events_fragment.view.*
import kotlinx.android.synthetic.main.events_venue_fragment.view.*
import kotlinx.android.synthetic.main.listing_fragment.*
import kotlinx.android.synthetic.main.listing_fragment.view.*

import kotlinx.android.synthetic.main.logout.view.*
import org.jetbrains.anko.doAsync
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.ArrayList
import kotlin.math.sin

class Events_Date : Fragment() {
    private val jsoncode = 1

    private var response: String? = null
    private var userlist: ListView? = null
    private var userArrayList: ArrayList<String>? = null
    private var userModelArrayList: ArrayList<User_Model>? = null
    private var customAdapter: CustomAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.events_date_fragment, container, false)
        var dataParsed:String? = ""

        view.go_back_button.setOnClickListener {
            (activity as NavigationHost).navigateTo(OptionsFragment(), false)
        }

        view.de.setOnClickListener {
            (activity as NavigationHost).navigateTo(JoinFragment(), false)
        }

        view.abcd.setOnClickListener {

            doAsync {

                val a = fetchInfo()
                //val x = JSONArray(a)
                val JA = JSONArray(a)


                for (i in 0..(JA.length() - 1)) {
                    val user = JA.getJSONObject(i)

                    println(four.text.toString())

                    if (user.get("eventname") == four.text.toString())
                    {

                        val singleParsed = "Eventname: " + user.get("eventname") + "   " + "Date: " + user.get("date") +  "   " +"EventID: " + user.get("id") + "\n"
                        dataParsed = dataParsed + singleParsed + "\n"

                    }
                    else
                    {
                        continue
                    }
                }
                //just set a text view to data parsed here
                var myTextView: TextView = view.textview4 as TextView
                myTextView.text = dataParsed


            }
        }

        return view;
    }




}

private fun fetchInfo(): String {
    val url = "https://project2final.appspot.com/androidlistevents"

    val client = OkHttpClient()
    val request = Request.Builder()
        .url(url)
        .header("User-Agent", "Android")
        .build()
    val response = client.newCall(request).execute()
    val bodystr =  response.body().string() // this can be consumed only once

    return bodystr
}

