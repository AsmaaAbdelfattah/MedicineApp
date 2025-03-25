package com.example.medicineremainder.Model

import java.util.UUID

data class Medicine(var medicineId:String = UUID.randomUUID().toString(), var name: String = "",
                    var durtion:String = "", var dose:String ="",
                    var remindMe:Boolean = true, var dates:MutableList<String> = mutableListOf(),
                    var time:String = "", var type:MedicineType = MedicineType.BILLS)

