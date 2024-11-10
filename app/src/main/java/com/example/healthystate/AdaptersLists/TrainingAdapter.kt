package com.example.healthystate.AdaptersLists

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.healthystate.API.APIClient
import com.example.healthystate.API.UserService
import com.example.healthystate.AutoAndReg.MainActivity
import com.example.healthystate.ChangeInfo.ChangeTrainingDialog
import com.example.healthystate.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class TrainingAdapter(


    private val structureList: MutableList<TrainingList>
) : RecyclerView.Adapter<TrainingAdapter.ViewHolder>() {
    private var intent: Intent? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_training, parent, false)
        intent = Intent(parent.context, ChangeTrainingDialog::class.java)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrainingAdapter.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val structure = structureList[position]
        val retrofit = APIClient.buildService(UserService::class.java)

        val getTraining: Call<List<WorkoutNameList>> = retrofit.getWorkoutNames()
        getTraining.enqueue(object : Callback<List<WorkoutNameList>> {
            override fun onResponse(call: Call<List<WorkoutNameList>>, response: Response<List<WorkoutNameList>>) {
                if (response.isSuccessful) {
                    val allWorkout: List<WorkoutNameList>? = response.body()
                    allWorkout?.forEach {

                        if (structure.workoutNameId == it.idWorkoutName) {
                            holder.groupNameTV.text = it.workoutName
                        }
                    }
                } else {
                }
            }

            override fun onFailure(call: Call<List<WorkoutNameList>>, t: Throwable) {
            }

        })

        holder.groupNameTV.text = structure.workoutNameId.toString()


        holder.itemView.setOnClickListener {

            intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent?.putExtra("idWorkout", structure.idWorkout)
            intent?.putExtra("dayWorkout", structure.dayWorkout)
            intent?.putExtra("logPassId", structure.logPassId)
            intent?.putExtra("workoutNameId", structure.workoutNameId)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return structureList.size
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var groupNameTV: TextView
        lateinit var groupDateTV: Date

        init {
            groupNameTV = view.findViewById(R.id.tvtraining)
        }
    }
}