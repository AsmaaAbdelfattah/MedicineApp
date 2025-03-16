package com.example.medicineremainder.Model

import android.provider.ContactsContract.CommonDataKinds.Email

data class User(var name:String = "",var email: String = "" ,
                var age:Int = 0, var medicine:MutableList<Medicine> = mutableListOf() )
