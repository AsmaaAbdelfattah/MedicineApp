package com.example.medicineremainder.Model

import android.provider.ContactsContract.CommonDataKinds.Email

data class User(var userId:String = "" ,var name:String = "",
                var email: String = "" , var age:Int = 0,
                var latitude: Double = 0.0, var longitude: Double = 0.0 ,
                var medicine:MutableList<Medicine> = mutableListOf() )
