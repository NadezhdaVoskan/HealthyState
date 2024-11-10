package com.example.healthystate.AdaptersLists

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.healthystate.API.APIClient
import com.example.healthystate.API.UserService
import com.example.healthystate.Models.*
import com.example.healthystate.Pages.ToDoList
import com.example.healthystate.Pages.Training
import com.example.healthystate.PaperDbClass
import com.example.healthystate.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class ToDoListAdapter (
    private val structureList: ArrayList<ToDoListList>
) : RecyclerView.Adapter<ToDoListAdapter.ViewHolder>() {
    private var intent: Intent? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoListAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_todo, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ToDoListAdapter.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val structure = structureList[position]
        holder.groupNameTV.text = structure.task

        val paperDbClass = PaperDbClass()
        val retrofit = APIClient.buildService(UserService::class.java)

        holder.complete.setOnCheckedChangeListener(null)
        holder.complete.isChecked = false

        retrofit.getToDoLists().enqueue(object : Callback<ArrayList<ToDoListList>>{
            override fun onResponse(
                call: Call<ArrayList<ToDoListList>>,
                response: Response<ArrayList<ToDoListList>>
            ) {
                if (response.isSuccessful){
                    val allToDoListList: List<ToDoListList>? = response.body()
                    allToDoListList?.forEach {
                        if (structure.idToDoList == it.idToDoList && paperDbClass.getUser()!!.idLogPass == it.logPassId){
                            if (it.statusList == true){
                                holder.complete.isChecked = it.statusList
                            }
                            return@forEach
                        }
                    }
                } else {

                }
            }

            override fun onFailure(call: Call<ArrayList<ToDoListList>>, t: Throwable) {

            }
        })

        holder.complete.setOnCheckedChangeListener { _, isChecked ->
            retrofit.getToDoLists().enqueue(object : Callback<ArrayList<ToDoListList>> {
                override fun onResponse(call: Call<ArrayList<ToDoListList>>, response: Response<ArrayList<ToDoListList>>) {
                    if (response.isSuccessful) {
                        val allStatus: List<ToDoListList>? = response.body()
                        allStatus?.forEach {
                            if (structure.idToDoList == it.idToDoList && paperDbClass.getUser()!!.idLogPass == it.logPassId) {
                                var obj = ChangeToDoListModel(it.idToDoList, it.task, paperDbClass.getUser()!!.idLogPass, isChecked)
                                retrofit.updateToDoLists(it.idToDoList, obj)
                                    .enqueue(object : Callback<DefaultResponse> {
                                        override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                            Toast.makeText(holder.itemView.context, "Mistake. Try again!", Toast.LENGTH_LONG).show()
                                        }

                                        override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                                            //Toast.makeText(holder.itemView.context, "ФЛАГ изменен!", Toast.LENGTH_LONG).show()

                                        }

                                    })
                                return@forEach
                            }
                        }


                    } else {
                        Toast.makeText(
                            holder.itemView.context,
                            "Недостоверные данные!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    // Обновить состояние флажка на экране
                    holder.complete.isChecked = isChecked
                    //notifyItemChanged(position)
                }

                override fun onFailure(call: Call<ArrayList<ToDoListList>>, t: Throwable) {

                }

            })
        }

        holder.image_view.setOnClickListener {
            retrofit.deleteToDoLists(structure.idToDoList)
                .enqueue(object : Callback<DefaultResponse> {
                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    }

                    override fun onResponse(
                        call: Call<DefaultResponse>,
                        response: Response<DefaultResponse>
                    ) {
                        val intentAct: Intent =
                            Intent(it.context, ToDoList::class.java)
                        it.context.startActivity(intentAct)
                    }

                })
        }
    }

    override fun getItemCount(): Int {
        return structureList.size
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var groupNameTV: TextView
        var image_view: ImageView
        var complete: CheckBox

        init {
            groupNameTV = view.findViewById(R.id.note_text)
            image_view = view.findViewById(R.id.delete)
            complete = view.findViewById(R.id.completed)
        }
    }
}