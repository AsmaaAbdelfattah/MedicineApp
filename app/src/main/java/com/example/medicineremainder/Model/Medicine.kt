package com.example.medicineremainder.Model

import java.util.UUID

data class Medicine(var medicineId:String = UUID.randomUUID().toString(), var name: String = "",
                    var frequency:String = "", var dose:String ="",
                    var remindMe:Boolean = true, var startDate:String = "" ,
                    var endDate:String = "",  var takenDates: MutableList<String> = mutableListOf(),
                    var time:String = "",var notes:String? = "",
                    var type:MedicineType = MedicineType.BILLS){
}

