package com.example.medicineremainder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import  com.example.medicineremainder.Model.News

class NewsAdapter(val list: ArrayList<News>):RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.newsTitle)
        val desc = itemView.findViewById<TextView>(R.id.newsdesc)
        val image = itemView.findViewById<ImageView>(R.id.newsImage)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.news_recycler,parent,false)
        return  NewsViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.title.text = list[position].title
        holder.desc.text = list[position].desc
        holder.image.setImageResource(list[position].image)

    }
}