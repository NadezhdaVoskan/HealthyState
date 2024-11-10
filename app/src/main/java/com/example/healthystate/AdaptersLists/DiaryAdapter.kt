package com.example.healthystate.AdaptersLists

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthystate.API.APIClient
import com.example.healthystate.API.UserService
import com.example.healthystate.AddInfo.AddDiaryTextDialog
import com.example.healthystate.ChangeInfo.ChangeDiaryTextDialog
import com.example.healthystate.ChangeInfo.ChangeTrainingDialog
import com.example.healthystate.Models.Mood
import com.example.healthystate.Pages.*
import com.example.healthystate.R
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DiaryAdapter (
    private val structureList: ArrayList<DiaryList>
) : RecyclerView.Adapter<DiaryAdapter.ViewHolder>() {
    private var intent: Intent? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_diary, parent, false)
        intent = Intent(parent.context, ChangeDiaryTextDialog::class.java)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: DiaryAdapter.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val structure = structureList[position]
        holder.groupNameTV.text = structure.nameNotes
        holder.groupDateTV.text = structure.dayPersonalDiary.dropLast(9)
        val retrofit = APIClient.buildService(UserService::class.java)

        val getMood: Call<MutableList<Mood>> = retrofit.getMood()
        getMood.enqueue(object : Callback<MutableList<Mood>> {
            override fun onResponse(call: Call<MutableList<Mood>>, response: Response<MutableList<Mood>>) {
                if (response.isSuccessful) {
                    val allMood: MutableList<Mood>? = response.body()
                    allMood?.forEach {
                        if (structure.moodId == it.idMood) {
                            holder.groupMoodTV.text = it.moodName
                        }
                    }
                } else {
                }
            }

            override fun onFailure(call: Call<MutableList<Mood>>, t: Throwable) {
            }

        })
        holder.groupMoodTV.text = structure.moodId.toString()
        val context = holder.groupNameTV.context
        holder.itemView.setOnClickListener {

            intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent?.putExtra("idPersonalDiary", structure.idPersonalDiary)
            intent?.putExtra("nameNotes", structure.nameNotes)
            intent?.putExtra("notes", structure.notes)
            intent?.putExtra("dayPersonalDiary", structure.dayPersonalDiary)
            intent?.putExtra("moodId", structure.moodId)
            intent?.putExtra("logPassId", structure.logPassId)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return structureList.size
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var groupNameTV: TextView
        var groupDateTV: TextView
        var groupMoodTV: TextView

        init {
            groupNameTV = view.findViewById(R.id.group_name)
            groupDateTV = view.findViewById(R.id.group_date)
            groupMoodTV = view.findViewById(R.id.group_mood)
        }
    }
}