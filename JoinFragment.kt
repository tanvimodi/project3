package com.example.getappengine

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.okhttp.MediaType
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.RequestBody
import kotlinx.android.synthetic.main.create_account_fragment.*
import kotlinx.android.synthetic.main.create_account_fragment.view.*
import kotlinx.android.synthetic.main.listing_fragment.view.*
import kotlinx.android.synthetic.main.login_fragment.view.*
import org.jetbrains.anko.doAsync


class JoinFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.join, container, false)
        // Set an error if the password is less than 8 characters.
        view.create_account_button2.setOnClickListener({
            val email = create_name_edit_text.text!!
            val eventid = create_email_edit_text.text!!



            doAsync {
                add_to_db(email, eventid)
            }

            (activity as NavigationHost).navigateTo(OptionsFragment(), false)
        })

        view.go_back_button.setOnClickListener {
            (activity as NavigationHost).navigateTo(OptionsFragment(), false)
        }

        return view
    }

    private fun add_to_db(user_email: Editable?, event_id: Editable?): String {
        val json = """
            {
            "user_email":"${user_email}",
            "event_id":"${event_id}"
            }
        """.trimIndent()

        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)

        val url = "https://project2final.appspot.com/user/join"

        val client =  OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .header("User-Agent", "Android")
            .post(body)
            .build()

        try {
            val response = client.newCall(request).execute()
            val bodystr =  response.body().string() // this can be consumed only once
            //println("worked"+bodystr)
            return bodystr
        }
        catch (e:Exception){
            println("Failed"+e.toString())
            return "error"
        }

    }
}