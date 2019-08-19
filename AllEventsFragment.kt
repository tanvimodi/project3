package com.example.getappengine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import kotlinx.android.synthetic.main.events_fragment.*
import kotlinx.android.synthetic.main.events_fragment.view.*
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.android.synthetic.main.login_fragment.view.*
import org.jetbrains.anko.doAsync
import org.json.JSONArray
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.create_account_fragment.view.*


class AllEventsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.events_fragment, container, false)


        view.button2.setOnClickListener {
            doAsync {
                val gotresponse = fetchInfo()
                val jsonarray = JSONArray(gotresponse)

                list_fun(jsonarray)
            }

        }
           // (activity as NavigationHost).navigateTo(JoinFragment(), false)



        return view

    }
}

private fun list_fun(jsonArray: JSONArray)
{



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
