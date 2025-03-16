package com.example.medicineremainder.Activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

import androidx.recyclerview.widget.RecyclerView
import com.example.medicineremainder.Fragments.HomeFragment
import com.example.medicineremainder.Fragments.MedicationsFragment
import com.example.medicineremainder.Model.News
import com.example.medicineremainder.NewsAdapter
import com.example.medicineremainder.R
import com.example.medicineremainder.Utilities.SharedPrefHelper
import com.google.firebase.firestore.FirebaseFirestore
import com.example.medicineremainder.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {
lateinit var homeBinding: MainActivityBinding
    lateinit var sharedPrefHelper: SharedPrefHelper
  //  lateinit var list: RecyclerView
  //  private val newsList = arrayListOf<News>() // ✅ Keep reference unchanged

    lateinit var adapter: NewsAdapter
    val dp = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding = MainActivityBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        enableEdgeToEdge()

        ///ADD FRAGMENTS
            replaceFragment(HomeFragment())
           homeBinding.bottomNav.setOnItemSelectedListener {
              when(it.itemId){
                  R.id.home -> replaceFragment(HomeFragment())
                  R.id.medicines -> replaceFragment(MedicationsFragment())
                  else ->{

                  }
              }
              true
           }


      //  list = findViewById(R.id.news_recycler)
        sharedPrefHelper = SharedPrefHelper(this)
        Toast.makeText(this,sharedPrefHelper.getUser().toString(),Toast.LENGTH_SHORT).show()
      //  saveToDatabase(News("seven","Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 15;",R.drawable.image))
//        adapter = NewsAdapter(newsList)
//        list.adapter = adapter
      //  readFromDatabase()
    }


    //Create framentes for bottom navigation bar
fun replaceFragment(fragment:Fragment){
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(homeBinding.fragmentContainer.id,fragment)
        fragmentTransaction.commit()
}
//    fun saveToDatabase(news: News) {
//
//        dp.collection("Users").add(news).addOnSuccessListener {
//            Toast.makeText(this,"Saved Successfully", Toast.LENGTH_SHORT).show()
//        }.addOnFailureListener {
//            Toast.makeText(this,"Failed to save", Toast.LENGTH_SHORT).show()
//        }
//
//    }
//
//    fun readFromDatabase() {
//
//        dp.collection("Users").get().addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                newsList.clear()
//                task.result?.forEach { document ->
//                    val news = document.toObject(News::class.java)
//                    if (news != null) {
//                        newsList.add(news)
//                    } else {
//                        Log.e("MainActivity", "Error converting document: ${document.id}")
//                    }
//                }
//                adapter.notifyDataSetChanged()
//            } else {
//                Log.e("MainActivity", "Error getting documents: ", task.exception)
//            }
//        }.addOnFailureListener {
//            Toast.makeText(this, "Failed to read", Toast.LENGTH_SHORT).show()
//        }
//    }
}
