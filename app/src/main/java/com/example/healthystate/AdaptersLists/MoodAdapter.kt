package com.example.healthystate.AdaptersLists

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthystate.Models.Mood
import com.example.healthystate.PaperDbClass
import com.example.healthystate.R
import io.paperdb.Paper

class MoodAdapter(
    val structureList: MutableList<Mood>
) : RecyclerView.Adapter<MoodAdapter.ViewHolder>() {
    private var selectedItemPosition: Int = -1
    private var intent: Intent? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoodAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_mood, parent, false)
        return ViewHolder(view)
    }

    fun setSelectedItemPosition(position: Int) {
        if (selectedItemPosition != position) {
            selectedItemPosition = position
            notifyDataSetChanged()
        }
    }

    override fun onBindViewHolder(holder: MoodAdapter.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val structure = structureList[position]
        var paperDbClass: PaperDbClass = PaperDbClass()


        holder.groupNameTV.text = structure.moodName
        val context = holder.groupNameTV.context
        holder.groupNameTV.setOnClickListener {
            setSelectedItemPosition(position)
            paperDbClass.setID(position+1)
        }

        if(selectedItemPosition == position)
            holder.cardView.setCardBackgroundColor(Color.parseColor("#e69dc1"))
        else
            holder.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
    }

    override fun getItemCount(): Int {
        return structureList.size
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var groupNameTV: TextView
        val cardView: CardView = itemView.findViewById<CardView>(R.id.cardviewMood)

        init {
            groupNameTV = view.findViewById(R.id.nameMood)
        }
    }
}