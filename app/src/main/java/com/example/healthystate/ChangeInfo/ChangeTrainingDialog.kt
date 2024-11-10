package com.example.healthystate.ChangeInfo

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.healthystate.API.APIClient
import com.example.healthystate.API.UserService
import com.example.healthystate.AdaptersLists.TrainingList
import com.example.healthystate.AdaptersLists.WorkoutNameList
import com.example.healthystate.Models.ChangeTrainingModel
import com.example.healthystate.Models.DefaultResponse
import com.example.healthystate.Models.TrainingModel
import com.example.healthystate.Pages.Training
import com.example.healthystate.PaperDbClass
import com.example.healthystate.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ChangeTrainingDialog : AppCompatActivity() {

    lateinit var dateEdt: EditText
    lateinit var nameWorkout: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.change_training_dialog)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }

        dateEdt = findViewById(R.id.idEdtDate)

        val spinner: Spinner = findViewById(R.id.Name_Workout)
        val training: ArrayList<Workout> = ArrayList()

        val retrofit = APIClient.buildService(UserService::class.java)

        val paperDbClass = PaperDbClass()

        val getTraining: Call<List<WorkoutNameList>> = retrofit.getWorkoutNames()
        getTraining.enqueue(object : Callback<List<WorkoutNameList>> {
            override fun onResponse(call: Call<List<WorkoutNameList>>, response: Response<List<WorkoutNameList>>) {
                if (response.isSuccessful) {
                    val allWorkout: List<WorkoutNameList>? = response.body()
                    allWorkout?.forEach {

                        training.add(Workout(it.workoutName))

                        val adapter = ArrayAdapter(
                            applicationContext,
                            android.R.layout.simple_spinner_dropdown_item,
                            training
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                        spinner.setAdapter(adapter)

                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Недостоверные данные!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<WorkoutNameList>>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "Ошибка со стороны клиента",
                    Toast.LENGTH_LONG
                ).show()
            }

        })

        val intent = getIntent()
        val idWorkout = intent.getIntExtra("idWorkout", 0)
        val dayWorkout = intent.getStringExtra("dayWorkout")
        val logPassId = intent.getIntExtra("logPassId", 0)
        val workoutNameId = intent.getIntExtra("workoutNameId", 0)
        dateEdt.setText(dayWorkout?.dropLast(9))
        spinner.post(Runnable { spinner.setSelection(workoutNameId - 1) })

        dateEdt.setOnClickListener {

            val c = Calendar.getInstance()


            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    val dat: String
                    if (dayOfMonth <= 10 && monthOfYear > 10) {
                        dat = (year.toString() + "-" + (monthOfYear + 1) + "-0" + dayOfMonth.toString())
                        dateEdt.setText(dat)
                    }
                    else if (dayOfMonth < 10 && monthOfYear < 10) {
                        dat = (year.toString() + "-0" + (monthOfYear + 1) + "-0" + dayOfMonth.toString())
                        dateEdt.setText(dat)
                    }
                    else if (monthOfYear < 9 && dayOfMonth > 10  ){
                        dat = (year.toString() + "-0" + (monthOfYear + 1) + "-" + dayOfMonth.toString())
                        dateEdt.setText(dat)
                    }
                    else {
                        dat = (year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString())
                        dateEdt.setText(dat)
                    }
                },
                year,
                month,
                day
            )

            datePickerDialog.show()
        }


        val delItem = findViewById<Button>(R.id.DelButWorkout)
        delItem.setOnClickListener{
            retrofit.deleteWorkout(idWorkout)
                .enqueue(object: Callback<DefaultResponse> {
                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, "Mistake. Try again!", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                        Toast.makeText(applicationContext, "Тренировка удалена!", Toast.LENGTH_SHORT).show()
                        val intent: Intent =
                            Intent(applicationContext, Training::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                        overridePendingTransition(0, 0)
                        finish()
                    }

                })
        }


        val updateItem = findViewById<Button>(R.id.ChangeButWorkout)
        updateItem.setOnClickListener{
            val obj = ChangeTrainingModel(idWorkout, dateEdt.text.toString().trim(), paperDbClass.getUser()!!.idLogPass, spinner.selectedItemId.toInt()+1)
            if (dateEdt.text.toString().equals(""))
            {
                dateEdt?.requestFocus()
                Toast.makeText(applicationContext, "Выберите дату!", Toast.LENGTH_SHORT).show()
            }
            else if (spinner.selectedItemId.toInt() == 0)
            {
                spinner.requestFocus()
            }
            else {
                retrofit.changeWorkout(idWorkout, obj)
                    .enqueue(object : Callback<DefaultResponse> {
                        override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                            Toast.makeText(
                                applicationContext,
                                "Mistake. Try again!",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        override fun onResponse(
                            call: Call<DefaultResponse>,
                            response: Response<DefaultResponse>
                        ) {
                            Toast.makeText(
                                applicationContext,
                                "Тренировка изменена!",
                                Toast.LENGTH_LONG
                            ).show()
                            val intent: Intent =
                                Intent(applicationContext, Training::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            startActivity(intent)
                            overridePendingTransition(0, 0)
                            finish()
                        }

                    })
            }
        }
    }


    private class Workout {
        var workout_name: String? = null

        constructor() {}
        constructor(workout_name: String?) {
            this.workout_name = workout_name
        }

        override fun toString(): String {
            return workout_name!!
        }
    }

}