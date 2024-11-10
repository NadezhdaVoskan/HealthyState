package com.example.healthystate.AdaptersLists

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthystate.Models.RecommendModel
import com.example.healthystate.Navigations.DetailsRecommend
import com.example.healthystate.R

class RecommendationAdapter (private val items: List<RecommendModel>) :
    RecyclerView.Adapter<RecommendationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_recommendation, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.imageView.setImageResource(item.imageResId)
        holder.titleTextView.text = item.title
        holder.itemView.setOnClickListener {
            // При нажатии на элемент списка можно показать дополнительные детали
            val intent = Intent(holder.itemView.context, DetailsRecommend::class.java)
            intent.putExtra("image", item.imageResId)
            intent.putExtra("title", item.title)
            intent.putExtra("preview", item.preview)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image_view)
        val titleTextView: TextView = itemView.findViewById(R.id.title_text_view)
        }
    }