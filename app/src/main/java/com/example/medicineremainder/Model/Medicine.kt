package com.example.medicineremainder.Model

data class Medicine(var name: String = "", var isPeremnant: Boolean = false ,
                    var durtion:String = "",var dose:String ="",var remindMe:Boolean = true,
                    var time:String, var type:MedicineType = MedicineType.BILLS)

