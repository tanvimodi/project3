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
import kotlinx.android.synthetic.main.events_fragment.view.*
import kotlinx.android.synthetic.main.listing_fragment.*
import kotlinx.android.synthetic.main.listing_fragment.view.*
import kotlinx.android.synthetic.main.logout.view.*
import kotlinx.android.synthetic.main.view_reservations.view.*
import org.jetbrains.anko.doAsync
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.ArrayList
import kotlin.math.sin

class ListingFragment : Fragment() {
    private val jsoncode = 1
    // Uncomment below if response is hardcoded instead of coming from a file asset
/*    private val response = """
    [
     {
      "name": "James",
      "email": "james@ut"
     },
     {
      "name": "Jean",
      "email": "jean@gmail"
     }
     ]
     """ */
    private var response: String? = null
    private var userlist: ListView? = null
    private var userArrayList: ArrayList<String>? = null
    private var userModelArrayList: ArrayList<User_Model>? = null
    private var customAdapter: CustomAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.listing_fragment, container, false)
        var dataParsed:String? = ""

        view.join_button.setOnClickListener {
            (activity as NavigationHost).navigateTo(JoinFragment(), false)
        }

        view.go_back_button.setOnClickListener {
            (activity as NavigationHost).navigateTo(OptionsFragment(), false)
        }

        doAsync {

            val a = fetchInfo()
            //val x = JSONArray(a)
            val JA = JSONArray(a)


            for (i in 0..(JA.length() - 1)) {
                val user = JA.getJSONObject(i)

                val singleParsed = "Event ID: " +  user.get("id") + "   " + "Event Name: " +  user.get("eventname") + "   " + "Date: " +user.get("date") + "\n"
                dataParsed = dataParsed + singleParsed +"\n"

            }
           //just set a text view to data parsed here
            var myTextView : TextView = view.textview1 as TextView
            myTextView.text = dataParsed




        }


        return view;
    }



    fun loadJSONFromAssets(): String? {
        var json: String? = null
        try {
            val inputStream = this.context?.assets?.open("users.json")
            val size = inputStream?.available()
            val buffer = ByteArray(size!!)
            inputStream.read(buffer)
            inputStream.close()

            json = String(buffer, Charsets.UTF_8)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return json
    }


    fun getStrings(response: String): ArrayList<String> {
        val userArrayList = ArrayList<String>()
        try {
            val dataArray = JSONArray(response)
            for (i in 0 until dataArray.length()) {
                val dataobj = dataArray.getJSONObject(i)
                userArrayList.add(dataobj.toString())
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return userArrayList
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

