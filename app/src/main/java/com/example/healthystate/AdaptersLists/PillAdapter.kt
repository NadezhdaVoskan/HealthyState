package com.example.healthystate.AdaptersLists

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthystate.ChangeInfo.ChangePillDialog
import com.example.healthystate.ChangeInfo.ChangeTrainingDialog
import com.example.healthystate.R
import java.util.*

class PillAdapter (
    private val structureList: MutableList<PillList>
) : RecyclerView.Adapter<PillAdapter.ViewHolder>() {
    private var intent: Intent? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PillAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_pill, parent, false)
        intent = Intent(parent.context, ChangePillDialog::class.java)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PillAdapter.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val structure = structureList[position]
        holder.groupNameTV.text = structure.pillName
        holder.groupCountTV.text = "Количество: "+ structure.countPill.toString()
        holder.groupTimeTV.text = "Время: " + structure.time!!.drop(11)

        holder.itemView.setOnClickListener {

            intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent?.putExtra("idPill", structure.idPill)
            intent?.putExtra("pillName", structure.pillName)
            intent?.putExtra("countPill", structure.countPill)
            intent?.putExtra("time", structure.time)
            intent?.putExtra("dayPill", structure.dayPill)
            intent?.putExtra("dayPillEnd", structure.dayPillEnd)
            intent?.putExtra("logPassId", structure.logPassId)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return structureList.size
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var groupNameTV: TextView
        var groupCountTV: TextView
        var groupTimeTV: TextView

        init {
            groupNameTV = view.findViewById(R.id.lblNamePill)
            groupCountTV = view.findViewById(R.id.lblCountPill)
            groupTimeTV = view.findViewById(R.id.lblTimePill)

        }
    }
}