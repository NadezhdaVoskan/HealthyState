package com.example.healthystate.AdaptersLists

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthystate.ChangeInfo.ChangeFoodDialog
import com.example.healthystate.ChangeInfo.ChangeTrainingDialog
import com.example.healthystate.Pages.*
import com.example.healthystate.R

class FoodAdapter (
    private val structureList: ArrayList<FoodList>
) : RecyclerView.Adapter<FoodAdapter.ViewHolder>() {
    private var intent: Intent? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_food, parent, false)
        intent = Intent(parent.context, ChangeFoodDialog::class.java)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodAdapter.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val structure = structureList[position]
        holder.groupNameTV.text = structure.nameFood
        holder.groupCaloriesTV.text = "Кал: " + structure.calories.toString()
        holder.groupGramsTV.text = structure.grams.toString() + " гр."

        holder.itemView.setOnClickListener {

            intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent?.putExtra("idFood", structure.idFood)
            intent?.putExtra("nameFood", structure.nameFood)
            intent?.putExtra("grams", structure.grams)
            intent?.putExtra("calories", structure.calories)
            intent?.putExtra("descriptionFood", structure.descriptionFood)
            intent?.putExtra("dayFood", structure.dayFood)
            intent?.putExtra("logPassId", structure.logPassId)
            intent?.putExtra("eatingId", structure.eatingId)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return structureList.size
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var groupNameTV: TextView
        var groupCaloriesTV: TextView
        var groupGramsTV: TextView

        init {
            groupNameTV = view.findViewById(R.id.lblListItem)
            groupCaloriesTV = view.findViewById(R.id.calories)
            groupGramsTV = view.findViewById(R.id.grams)

        }
    }
}