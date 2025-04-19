package com.example.medicineremainder.Fragments


import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medicineremainder.Adapters.CalendarAdapter
import com.example.medicineremainder.Model.Medicine
import com.example.medicineremainder.Model.MedicineType
import com.example.medicineremainder.R
import com.example.medicineremainder.Utilities.FirebaseManager
import com.example.medicineremainder.databinding.FragmentAddMedicineBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID


class AddMedicineFragment : Fragment() {
   lateinit var binding: FragmentAddMedicineBinding
    lateinit var calendarAdapter: CalendarAdapter
    val calendar = Calendar.getInstance()
     var medicine: Medicine = Medicine()
    val selectedDays = mutableListOf<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddMedicineBinding.inflate(layoutInflater,container,false)
        //TODO intial types , dates and time

        handleMedicineType()
        handleListeners()
        resetScreen()


        return binding.root
    }
    //TODO handle init screen, listeners
    fun  resetScreen(){
        refreshCalender()
        binding.capsule.setBackgroundResource(R.drawable.selected_day_calender)
        binding.mediceNameEt.setText("")
        binding.doseEdit.setText("")
        binding.notesEt.setText("")
        medicine.frequency = "24 hr"
         medicine.type = MedicineType.BILLS
        var adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, resources.getStringArray(R.array.intervalsList))
        binding.intervalSpinner.setAdapter(adapter)
        binding.intervalSpinner.setSelection(0)
    }
    fun handleListeners(){
          binding.saveBtn.setOnClickListener {
              if (validateData()){
                  medicine.medicineId = UUID.randomUUID().toString()
                  binding.progressBar.visibility = View.VISIBLE
                  FirebaseManager.addMedicineToUser(requireContext(),medicine){ success ->
                      binding.progressBar.visibility = View.GONE
                 if (success) {
                     FirebaseManager.currentUserFromDB(requireContext()){ user ->
                         if (user != null) {
                             Toast.makeText(requireContext(), R.string.saved_successfully, Toast.LENGTH_SHORT).show()
                             resetScreen()
                         }
                     }
                 }else{
                     Toast.makeText(requireContext(), "Cannots amm", Toast.LENGTH_SHORT).show()
                 }
                }
              }
          }
          binding.timeLinear.setOnClickListener {
              showTimePicker()
          }
        binding.intervalSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // Get the selected item
                val selectedItem = parent.getItemAtPosition(position).toString()
                medicine.frequency = parent.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // This method is not always called, but you can handle it here if needed
            }
        }

        binding.btnNextMonth.setOnClickListener {
            calendar.add(Calendar.MONTH, 1)
            refreshCalender()
        }
        binding.btnPrevMonth.setOnClickListener {
            calendar.add(Calendar.MONTH, -1)
            refreshCalender()
        }
      }

    fun refreshCalender(){
        val days = getDaysOfMonth()
        // Select today's date initially
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
        if (days.second.contains(today)) {
            selectedDays.add(today)
        }

        // Set up RecyclerView
        calendarAdapter = CalendarAdapter(days, selectedDays) { updatedSelection ->
            selectedDays.clear()
            selectedDays.addAll(updatedSelection)
        }

        binding.calenderrecyclerView.apply {
            adapter = calendarAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            // Scroll to today's date
            val todayIndex = days.second.indexOf(today)
            if (todayIndex != -1) {
                postDelayed({ scrollToPosition(todayIndex) }, 200) // Smooth scroll
            }
        }
    }
    fun handleMedicineType(){
        binding.capsule.setOnClickListener {
            binding.capsule.setBackgroundResource(R.drawable.selected_day_calender)
            binding.drink.setBackgroundResource(R.drawable.borders)
            binding.cream.setBackgroundResource(R.drawable.borders)
            binding.injection.setBackgroundResource(R.drawable.borders)
            medicine.type = MedicineType.BILLS
        }
        binding.cream.setOnClickListener {
            binding.capsule.setBackgroundResource(R.drawable.borders)
            binding.drink.setBackgroundResource(R.drawable.borders)
            binding.injection.setBackgroundResource(R.drawable.borders)
            binding.cream.setBackgroundResource(R.drawable.selected_day_calender)
            medicine.type = MedicineType.CREAM
        }
        binding.injection.setOnClickListener {
            binding.capsule.setBackgroundResource(R.drawable.borders)
            binding.drink.setBackgroundResource(R.drawable.borders)
            binding.cream.setBackgroundResource(R.drawable.borders)
            binding.injection.setBackgroundResource(R.drawable.selected_day_calender)
            medicine.type = MedicineType.INJECTION
        }
        binding.drink.setOnClickListener {
            binding.capsule.setBackgroundResource(R.drawable.borders)
            binding.drink.setBackgroundResource(R.drawable.selected_day_calender)
            binding.cream.setBackgroundResource(R.drawable.borders)
            binding.injection.setBackgroundResource(R.drawable.borders)
            medicine.type = MedicineType.DRINK
        }
    }
//TODo handle create month days
fun getDaysOfMonth(): Pair<List<String>, List<String>> {
    val calendar = Calendar.getInstance()
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val days = mutableListOf<String>()
    val weekDays = mutableListOf<String>()

    val weekDayNames = arrayOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN")

    // Get the first day of the month
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1 // Adjust for 0-based index

    // Populate actual days
    for (day in 1..daysInMonth) {
        calendar.set(Calendar.DAY_OF_MONTH, day)
        val weekDayIndex = calendar.get(Calendar.DAY_OF_WEEK) - 1
        val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)

        days.add(formattedDate) // Store date in YYYY-MM-DD format
        weekDays.add(weekDayNames[weekDayIndex])
    }

    return Pair(weekDays, days)
}
    //TODO handle create time picker
    fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
            // Convert to 12-hour format & get AM/PM
            val formattedTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(
                Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, selectedHour)
                    set(Calendar.MINUTE, selectedMinute)
                }.time
            )

            // Set the selected time
            medicine.time = formattedTime

            val currentText = resources.getText(R.string.please_select_dates_for_reminder)
            binding.selectTime.setText("$currentText, $formattedTime") // Append selected time

        }, hour, minute, false) // `false` forces 12-hour format

        timePickerDialog.show()
    }
    //TODO handle init screen
    fun validateData():Boolean{
        if (binding.mediceNameEt.text.isEmpty()) {
            Toast.makeText(requireContext(),
                getString(R.string.please_enter_medicine_name), Toast.LENGTH_SHORT).show()
            return false
        }else{
            medicine.name = binding.mediceNameEt.text.toString()
        }
//        if (binding.doseEdit.text.isEmpty()) {
//            Toast.makeText(requireContext(),
//                getString(R.string.please_enter_medicine_name), Toast.LENGTH_SHORT).show()
//            //return false
//        }else{
            medicine.dose = binding.doseEdit.text.toString()
        //}
        if (selectedDays.size == 0){
            Toast.makeText(requireContext(),
                getString(R.string.please_select_dates_for_reminder),Toast.LENGTH_SHORT).show()
            return false
        }else{
            medicine.startDate = selectedDays.first()
            medicine.endDate = selectedDays.last()
        }
        if (medicine.frequency.isEmpty()) {
            Toast.makeText(requireContext(),
                getString(R.string.please_choose_interval), Toast.LENGTH_SHORT).show()
            return false
        }
        if (medicine.time.isEmpty()) {
            Toast.makeText(requireContext(),
                getString(R.string.please_choose_time), Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
    }