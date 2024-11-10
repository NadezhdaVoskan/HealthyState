package com.example.healthystate.AdaptersLists

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.healthystate.API.APIClient
import com.example.healthystate.API.UserService
import com.example.healthystate.Models.*
import com.example.healthystate.PaperDbClass
import com.example.healthystate.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class CheckUpAdapter(
    private val structureList: MutableList<CheckUpList>
) : RecyclerView.Adapter<CheckUpAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckUpAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_doctor, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CheckUpAdapter.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val structure = structureList[position]
        holder.groupNameTV.text = structure.doctorTestName

        val paperDbClass = PaperDbClass()
        val retrofit = APIClient.buildService(UserService::class.java)

        var obj : ChangeCheckUpModel


        holder.complete.setOnCheckedChangeListener(null)
        holder.complete.isChecked = false // Сброс состояния флажка

        retrofit.getCheckUpView().enqueue(object : Callback<List<CheckUpViewList>> {
            override fun onResponse(call: Call<List<CheckUpViewList>>, response: Response<List<CheckUpViewList>>) {
                if (response.isSuccessful) {
                    val allStatus: List<CheckUpViewList>? = response.body()
                    allStatus?.forEach {
                        if (structure.idDoctorTest == it.doctorTestId && paperDbClass.getUser()!!.idLogPass == it.logPassId) {
                            if (it.statusDoctorTest == true){
                                holder.complete.isChecked = it.statusDoctorTest}
                            if (it.dataCheck != null) {
                                holder.noteDate.text = it.dataCheck.dropLast(9)
                            }
                            return@forEach
                        }
                    }
                } else {
                    // Обработка ошибки
                }
            }

            override fun onFailure(call: Call<List<CheckUpViewList>>, t: Throwable) {
                // Обработка ошибки
            }
        })

        holder.complete.setOnCheckedChangeListener { _, isChecked ->
            val today = LocalDate.now().toString()

            retrofit.getCheckUpView().enqueue(object : Callback<List<CheckUpViewList>> {
                override fun onResponse(call: Call<List<CheckUpViewList>>, response: Response<List<CheckUpViewList>>) {
                    if (response.isSuccessful) {
                        var foundFlag = false
                        val allStatus: List<CheckUpViewList>? = response.body()
                        allStatus?.forEach {
                            if (structure.idDoctorTest == it.doctorTestId && paperDbClass.getUser()!!.idLogPass == it.logPassId) {
                                foundFlag = true
                                obj = if (!it.statusDoctorTest){
                                    ChangeCheckUpModel(it.idDoctorTestView, it.doctorTestId, paperDbClass.getUser()!!.idLogPass, isChecked, today)
                                } else {
                                    if (it.dataCheck != null) {
                                        ChangeCheckUpModel(it.idDoctorTestView, it.doctorTestId, paperDbClass.getUser()!!.idLogPass, isChecked, it.dataCheck)
                                    } else {
                                        ChangeCheckUpModel(it.idDoctorTestView, it.doctorTestId, paperDbClass.getUser()!!.idLogPass, isChecked, today)
                                    }
                                }

                                retrofit.updateDoctorTestView(it.idDoctorTestView, obj)
                                    .enqueue(object : Callback<DefaultResponse> {
                                        override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                            Toast.makeText(holder.itemView.context, "Mistake. Try again!", Toast.LENGTH_LONG).show()
                                        }

                                        override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                                            //Toast.makeText(holder.itemView.context, "ФЛАГ изменен!", Toast.LENGTH_LONG).show()

                                            retrofit.getCheckUpView().enqueue(object : Callback<List<CheckUpViewList>> {
                                                override fun onResponse(call: Call<List<CheckUpViewList>>, response: Response<List<CheckUpViewList>>) {
                                                    if (response.isSuccessful) {
                                                        val allStatus: List<CheckUpViewList>? = response.body()
                                                        allStatus?.forEach {
                                                            if (structure.idDoctorTest == it.doctorTestId && paperDbClass.getUser()!!.idLogPass == it.logPassId) {
                                                                if (it.statusDoctorTest == true){
                                                                    holder.complete.isChecked = it.statusDoctorTest}
                                                                if (it.dataCheck != null) {
                                                                    holder.noteDate.text = it.dataCheck.dropLast(9)
                                                                }
                                                                return@forEach
                                                            }
                                                        }
                                                    } else {
                                                        // Обработка ошибки
                                                    }
                                                }

                                                override fun onFailure(call: Call<List<CheckUpViewList>>, t: Throwable) {
                                                    // Обработка ошибки
                                                }
                                            })
                                        }

                                    })
                                return@forEach
                            }
                        }
                        if (!foundFlag) {
                            val obj = CheckUpViewModel(structure.idDoctorTest, paperDbClass.getUser()!!.idLogPass, isChecked, LocalDate.now().toString())

                            retrofit.createDoctorTestViews(obj)
                                .enqueue(object : Callback<DefaultResponse> {
                                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                        Toast.makeText(holder.itemView.context, "Mistake. Try again!", Toast.LENGTH_LONG).show()
                                    }

                                    override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                                       // Toast.makeText(holder.itemView.context, "ФЛАГ добавлен!", Toast.LENGTH_LONG).show()

                                        notifyItemChanged(position)
                                        retrofit.getCheckUpView().enqueue(object : Callback<List<CheckUpViewList>> {
                                            override fun onResponse(call: Call<List<CheckUpViewList>>, response: Response<List<CheckUpViewList>>) {
                                                if (response.isSuccessful) {
                                                    val allStatus: List<CheckUpViewList>? = response.body()
                                                    allStatus?.forEach {
                                                        if (structure.idDoctorTest == it.doctorTestId && paperDbClass.getUser()!!.idLogPass == it.logPassId) {
                                                            if (it.statusDoctorTest == true){
                                                                holder.complete.isChecked = it.statusDoctorTest}
                                                            if (it.dataCheck != null) {
                                                                holder.noteDate.text = it.dataCheck.dropLast(9)
                                                            }
                                                            return@forEach
                                                        }
                                                    }
                                                } else {
                                                    // Обработка ошибки
                                                }
                                            }

                                            override fun onFailure(call: Call<List<CheckUpViewList>>, t: Throwable) {
                                                // Обработка ошибки
                                            }
                                        })
                                    }

                                })

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
                }

                override fun onFailure(call: Call<List<CheckUpViewList>>, t: Throwable) {
                    // Обработка ошибки
                }
            })
        }
    }


    override fun getItemCount(): Int {
        return structureList.size
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var groupNameTV: TextView
        var complete: CheckBox
        var noteDate: TextView

        init {
            groupNameTV = view.findViewById(R.id.note_text_doctor)
            complete = view.findViewById(R.id.completed_doctor)
            noteDate = view.findViewById(R.id.note_date_doctor)


        }
    }


}