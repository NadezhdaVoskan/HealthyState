package com.example.healthystate.AddInfo

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.healthystate.API.APIClient
import com.example.healthystate.API.UserService
import com.example.healthystate.AdaptersLists.WorkoutNameList
import com.example.healthystate.Models.DefaultResponse
import com.example.healthystate.Models.ToDoListsModel
import com.example.healthystate.Models.TrainingNameModel
import com.example.healthystate.Pages.ToDoList
import com.example.healthystate.Pages.Training
import com.example.healthystate.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class AddTrainingName : AppCompatActivity() {

    lateinit var txtName: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }
        setContentView(R.layout.add_training_name)

        val spinner: Spinner = findViewById(R.id.spinnernworkout)

        txtName = findViewById(R.id.nameworkout)

        val retrofit = APIClient.buildService(UserService::class.java)

        val addBtn = findViewById<Button>(R.id.addworkout)
        addBtn.setOnClickListener{

            val obj = TrainingNameModel(txtName.text.toString().trim())

            if (txtName.text.toString().equals(""))
            {
                txtName?.requestFocus()
                Toast.makeText(applicationContext, "Введите название активности!", Toast.LENGTH_SHORT).show()
            } else {
            retrofit.createTrainingName(obj)
                .enqueue(object: Callback<DefaultResponse> {
                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, "Mistake. Try again!", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                        Toast.makeText(applicationContext, "Вид активности добавлен!", Toast.LENGTH_SHORT).show()
                        val intent: Intent =
                            Intent(applicationContext, AddTrainingName::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                        overridePendingTransition(0, 0)
                        finish()
                    }

                })
        }}

        val training: ArrayList<Workout> = ArrayList()
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

        val delItem = findViewById<Button>(R.id.delworkout)
        delItem.setOnClickListener{

            retrofit.deleteTrainingName(spinner.selectedItemPosition)
                .enqueue(object: Callback<DefaultResponse> {
                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, "Mistake. Try again!", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {

                        Toast.makeText(applicationContext, "Вид тренировки удален!", Toast.LENGTH_SHORT).show()
                        val intent: Intent =
                            Intent(applicationContext, AddTrainingName::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                        finish()
                    }

                })
        }

    }
    private class Workout {
        var workout_name: String? = null

        constructor(workout_name: String?) {
            this.workout_name = workout_name
        }

        override fun toString(): String {
            return workout_name!!
        }
    }

}