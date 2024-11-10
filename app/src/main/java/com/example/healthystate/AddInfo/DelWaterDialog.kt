package com.example.healthystate.AddInfo

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.healthystate.API.APIClient
import com.example.healthystate.API.UserService
import com.example.healthystate.AdaptersLists.WaterList
import com.example.healthystate.Models.ChangeWaterModel
import com.example.healthystate.Models.DefaultResponse
import com.example.healthystate.Models.WaterModel
import com.example.healthystate.Pages.WaterBalance
import com.example.healthystate.PaperDbClass
import com.example.healthystate.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class DelWaterDialog :  DialogFragment()  {

    lateinit var dateEdt: EditText
    lateinit var  amountEdt: EditText
    var amountint: Int = 0
    var dayStatus: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getDialog()!!.getWindow()?.setBackgroundDrawableResource(R.drawable.roundcornerdialog);
        val view: View = inflater!!.inflate(R.layout.delwater_window, container, false)

        dateEdt = view.findViewById(R.id.idEdtDate)
        amountEdt = view.findViewById(R.id.Amount_Water)

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

        val retrofit = APIClient.buildService(UserService::class.java)
        val paperDbClass = PaperDbClass()

        amountEdt.addTextChangedListener(object : TextWatcher {
            var editing = false
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!editing) {
                    editing = true
                    if (s?.toString()?.startsWith("0") == true) {
                        amountEdt.setText(s.subSequence(1, s.length))
                        amountEdt.setSelection(amountEdt.text.length)
                    }
                    editing = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (!editing) {
                    editing = true
                    if (s.toString().isEmpty()) {
                        s?.replace(0, s.length, "0")
                    }
                    editing = false
                }
            }
        })

        val delBtn = view.findViewById<Button>(R.id.DelButWater)
        delBtn.setOnClickListener {
            val getDayUnick: Call<List<WaterList>> = retrofit.getWaters()
            getDayUnick.enqueue(object : Callback<List<WaterList>> {
                override fun onResponse(call: Call<List<WaterList>>, response: Response<List<WaterList>>) {
                    if (response.isSuccessful) {
                        val allDayWater: List<WaterList>? = response.body()
                        var found = false
                        allDayWater?.forEach { waterList ->
                            if (dateEdt.text.toString() == waterList.dayWater.dropLast(9)) {
                                found = true
                                amountint = amountEdt.text.toString().trim().toInt()
                                val obj = ChangeWaterModel(
                                    waterList.idWater,
                                    waterList.amount - amountint,
                                    dateEdt.text.toString().trim(),
                                    paperDbClass.getUser()!!.idLogPass
                                )
                                if (amountEdt.text.toString().isEmpty()) {
                                    amountEdt.requestFocus()
                                    Toast.makeText(activity?.applicationContext, "Введите количество!", Toast.LENGTH_SHORT).show()
                                } else if (amountEdt.text.startsWith("0")) {
                                    amountEdt.requestFocus()
                                    Toast.makeText(activity?.applicationContext, "Количество воды не может быть отрицательным числом или равно 0!", Toast.LENGTH_SHORT).show()
                                } else if (amountint <= 0 || amountint > waterList.amount) {
                                    amountEdt.requestFocus()
                                    Toast.makeText(activity?.applicationContext, "Количество воды не может быть отрицательным числом или превышать текущее количество!", Toast.LENGTH_SHORT).show()
                                } else if (dateEdt.text.toString().isEmpty()) {
                                    dateEdt.requestFocus()
                                    Toast.makeText(activity?.applicationContext, "Выберите дату!", Toast.LENGTH_SHORT).show()
                                } else {
                                    retrofit.changeWaters(waterList.idWater, obj)
                                        .enqueue(object : Callback<DefaultResponse> {
                                            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                                Toast.makeText(activity?.applicationContext, "Mistake. Try again!", Toast.LENGTH_SHORT).show()
                                            }

                                            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                                                Toast.makeText(activity?.applicationContext, "Количество воды уменьшилось!", Toast.LENGTH_SHORT).show()
                                                val intent = Intent(activity?.applicationContext, WaterBalance::class.java)
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                                startActivity(intent)
                                                activity?.finish()
                                            }

                                        })
                                }
                            }
                        }
                        if (!found) {
                            amountint = amountEdt.text.toString().trim().toInt()
                            val obj = WaterModel(amountint, dateEdt.text.toString().trim(), paperDbClass.getUser()!!.idLogPass)
                            if (amountEdt.text.toString().isEmpty()) {
                                amountEdt.requestFocus()
                                Toast.makeText(activity?.applicationContext, "Введите количество!", Toast.LENGTH_SHORT).show()
                            } else if (amountEdt.text.startsWith("0")) {
                                amountEdt.requestFocus()
                                Toast.makeText(activity?.applicationContext, "Количество воды не может быть отрицательным числом или равно 0!", Toast.LENGTH_SHORT).show()
                            } else if (amountint <= 0) {
                                amountEdt.requestFocus()
                                Toast.makeText(activity?.applicationContext, "Количество воды не может быть отрицательным числом или равно 0!", Toast.LENGTH_SHORT).show()
                            } else if (dateEdt.text.toString().isEmpty()) {
                                dateEdt.requestFocus()
                                Toast.makeText(activity?.applicationContext, "Выберите дату!", Toast.LENGTH_SHORT).show()
                            } else {
                                retrofit.createWaters(obj)
                                    .enqueue(object : Callback<DefaultResponse> {
                                        override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                            Toast.makeText(activity?.applicationContext, "Mistake. Try again!", Toast.LENGTH_SHORT).show()
                                        }

                                        override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                                            Toast.makeText(activity?.applicationContext, "Количество воды уменьшилось!", Toast.LENGTH_SHORT).show()
                                        }
                                    })
                            }
                        }
                    } else {
                        Toast.makeText(activity?.applicationContext, "Недостоверные данные!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<WaterList>>, t: Throwable) {
                    Toast.makeText(activity?.applicationContext, "Ошибка со стороны клиента", Toast.LENGTH_SHORT).show()
                }
            })
        }

        return view;
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}