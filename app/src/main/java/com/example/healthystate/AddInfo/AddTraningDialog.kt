package com.example.healthystate.AddInfo

import android.app.DatePickerDialog
import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.example.healthystate.API.APIClient
import com.example.healthystate.API.UserService
import com.example.healthystate.AdaptersLists.EatingList
import com.example.healthystate.AdaptersLists.TrainingList
import com.example.healthystate.AdaptersLists.WorkoutNameList
import com.example.healthystate.Models.DefaultResponse
import com.example.healthystate.Models.TrainingModel
import com.example.healthystate.Navigations.NavigationForThemes
import com.example.healthystate.Pages.Training
import com.example.healthystate.PaperDbClass
import com.example.healthystate.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class AddTraningDialog:  DialogFragment()  {

    lateinit var dateEdt: EditText
    lateinit var nameWorkout: String
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getDialog()!!.getWindow()?.setBackgroundDrawableResource(R.drawable.roundcornerdialog);
        val view: View = inflater!!.inflate(R.layout.addtraining_window, container, false)


        dateEdt = view.findViewById(R.id.idEdtDate)

        val spinner: Spinner = view.findViewById(R.id.Name_Workout)

        dateEdt.setOnClickListener {

            val c = Calendar.getInstance()


            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                view.context,
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
                            activity!!.applicationContext,
                            android.R.layout.simple_spinner_dropdown_item,
                            training
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                        spinner.setAdapter(adapter)

                    }
                } else {
                    Toast.makeText(
                        activity?.applicationContext,
                        "Недостоверные данные!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<WorkoutNameList>>, t: Throwable) {
                Toast.makeText(
                    activity?.applicationContext,
                    "Ошибка со стороны клиента",
                    Toast.LENGTH_LONG
                ).show()
            }

        })

        val addBtn = view.findViewById<Button>(R.id.AddButWorkout)
        addBtn.setOnClickListener{

            val obj = TrainingModel(dateEdt.text.toString().trim(), paperDbClass.getUser()!!.idLogPass, spinner.selectedItemId.toInt()+1)

            if (dateEdt.text.toString().equals(""))
            {
                dateEdt?.requestFocus()
                Toast.makeText(activity?.applicationContext, "Выберите дату!", Toast.LENGTH_SHORT).show()
            }
            else if (spinner.selectedItemId.toInt() == 0)
            {
                spinner.requestFocus()
            }
            else {
            retrofit.createWorkout(obj)
                .enqueue(object: Callback<DefaultResponse> {
                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        Toast.makeText(activity?.applicationContext, "Mistake. Try again!", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                        val intent = Intent(activity?.applicationContext, Training::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        activity?.startActivity(intent)
                        activity?.overridePendingTransition(0, 0)
                        activity?.finish()
                    }
                })
        }}

        return view;

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

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}