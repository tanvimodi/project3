package com.example.getappengine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.android.synthetic.main.login_fragment.view.*
import org.jetbrains.anko.doAsync
import org.json.JSONArray

class LoginFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.login_fragment, container, false)


        view.login_button.setOnClickListener {
            doAsync {
                val gotresponse = fetchInfo()
                val jsonarray = JSONArray(gotresponse)
                print(gotresponse)


                for (i in 0..(jsonarray.length() - 1)) {
                    val user = jsonarray.getJSONObject(i)
                    println(user)

                    if (user.get("name") == name_edit_text.text.toString() && user.get("email") == email_edit_text.text.toString()) {

                        //navigate to events fragment
                        (activity as NavigationHost).navigateTo(OptionsFragment(), false)

                    }
                    if (user.get("name") != name_edit_text.text.toString() && user.get("email") != email_edit_text.text.toString())  {
                        Answers.setVisibility(View.VISIBLE);
                    }
                }

            }
        }

        view.create_account_button.setOnClickListener{
            (activity as NavigationHost).navigateTo(CreateAccountFragment(), false)
        }

        return view
    }
}

private fun fetchInfo(): String {
    val url = "https://project2final.appspot.com/loginandroid"

    val client = OkHttpClient()
    val request = Request.Builder()
        .url(url)
        .header("User-Agent", "Android")
        .build()
    val response = client.newCall(request).execute()
    val bodystr =  response.body().string() // this can be consumed only once

    return bodystr
}
