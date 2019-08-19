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
import kotlinx.android.synthetic.main.login_fragment.view.*
import org.jetbrains.anko.doAsync


class CreateAccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.create_account_fragment, container, false)
        // Set an error if the password is less than 8 characters.
        view.create_account_button2.setOnClickListener({
            val name = create_name_edit_text.text!!
            val email = create_email_edit_text.text!!
            val instrument = create_instrument_edit_text.text!!

            doAsync {
                add_to_db(name, email, instrument)
            }

            (activity as NavigationHost).navigateTo(OptionsFragment(), false)
        })

        return view
    }

    private fun add_to_db(name: Editable?, email: Editable?, instrument: Editable?): String {
        val json = """
            {
                "name":"${name}",
                "email":"${email}",
                "instrument":"${instrument}"
            }
            """.trimIndent()

        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)

        val url = "https://project2final.appspot.com/user/add"

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