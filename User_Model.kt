package com.example.getappengine

import java.util.*

class User_Model {
    var date:String? = null
    var eventname: String? = null


    fun getNames(): String {
        return date.toString()
    }

    fun setNames(name: String) {
        this.date = name
    }




    fun getInstruments(): String {
        return eventname.toString()
    }

    fun setInstruments(name: String) {
        this.eventname = name
    }

}