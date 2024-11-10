package com.example.healthystate.Navigations

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthystate.Pages.*
import com.example.healthystate.R

class MyListAdapter (
    private val context: Context,
    private val structureList: ArrayList<StructureInfo>
    ) : RecyclerView.Adapter<MyListAdapter.ViewHolder>() {
        private var intent: Intent? = null
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(context).inflate(R.layout.my_custom_list_navigation, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
            val structure = structureList[position]
            holder.groupNameTV.text = structure.name
            holder.groupPictView.setBackgroundResource(structure.image)
            val context = holder.groupNameTV.context
            holder.groupNameTV.setOnClickListener {
                when (position) {
                    0 -> intent = Intent(context, Training::class.java)
                    1 -> intent = Intent(context, Pill::class.java)
                    2 -> intent = Intent(context, CheckupDoctor::class.java)
                    3 -> intent = Intent(context, Food::class.java)
                    4 -> intent = Intent(context, WaterBalance::class.java)
                    5 -> intent = Intent(context, PersonalDiary::class.java)
                    6 -> intent = Intent(context, ToDoList::class.java)
                    //else -> Toast.makeText(context, structure.getName(), Toast.LENGTH_SHORT).show()
                }
                context.startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return structureList.size
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var groupNameTV: TextView
            var groupPictView: ImageView

            init {
                groupNameTV = view.findViewById(R.id.group_name)
                groupPictView = view.findViewById(R.id.pict)
            }
        }
}