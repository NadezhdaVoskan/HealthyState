package com.example.healthystate.ChangeInfo

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.example.healthystate.API.APIClient
import com.example.healthystate.API.UserService
import com.example.healthystate.Models.ChangePillModel
import com.example.healthystate.Models.ChangeTrainingModel
import com.example.healthystate.Models.DefaultResponse
import com.example.healthystate.Models.PillModel
import com.example.healthystate.Pages.Pill
import com.example.healthystate.Pages.Training
import com.example.healthystate.PaperDbClass
import com.example.healthystate.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ChangePillDialog : AppCompatActivity() {

    lateinit var edtName: EditText
    lateinit var edtCount: EditText
    lateinit var txtTime: TextView
    lateinit var dateEdt_Start: EditText
    lateinit var dateEdt_End: EditText
    var count: Int = 0
    private val timePickerDialogListener: TimePickerDialog.OnTimeSetListener =
        object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {

                val formattedTime: String = when {
                    hourOfDay == 0 -> {
                        if (minute < 10) {
                            "${hourOfDay + 12}:0${minute}:00"
                        } else {
                            "${hourOfDay + 12}:${minute}:00"
                        }
                    }
                    hourOfDay > 12 -> {
                        if (minute < 10) {
                            "${hourOfDay - 12}:0${minute}:00"
                        } else {
                            "${hourOfDay - 12}:${minute}:00"
                        }
                    }
                    hourOfDay == 12 -> {
                        if (minute < 10) {
                            "${hourOfDay}:0${minute}:00"
                        } else {
                            "${hourOfDay}:${minute}:00"
                        }
                    }
                    else -> {
                        if (minute < 10) {
                            "${hourOfDay}:${minute}:00"
                        } else {
                            "${hourOfDay}:${minute}:00"
                        }
                    }
                }

                txtTime.text = "$formattedTime"
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.change_pill_dialog)

        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }

        val buttonPickTime: Button = findViewById(R.id.PickTime)
        txtTime = findViewById(R.id.txtTime)

        buttonPickTime.setOnClickListener {
            val timePicker: TimePickerDialog = TimePickerDialog(
                this,
                timePickerDialogListener,
                12,
                10,
                false
            )

            timePicker.show()
        }

        dateEdt_Start = findViewById(R.id.idEdtDateStart)
        dateEdt_End = findViewById(R.id.idEdtDateEnd)
        edtName = findViewById(R.id.Name_Pill)
        edtCount = findViewById(R.id.CountPill)


        val intent = getIntent()
        val idPill = intent.getIntExtra("idPill", 0)
        val pillName = intent.getStringExtra("pillName")
        val countPill = intent.getIntExtra("countPill", 0)
        val time = intent.getStringExtra("time")
        val dayPill = intent.getStringExtra("dayPill")
        val dayPillEnd = intent.getStringExtra("dayPillEnd")
        val logPassId = intent.getIntExtra("logPassId", 0)

        txtTime.setText(time?.drop(11))
        dateEdt_Start.setText(dayPill?.dropLast(9))
        dateEdt_End.setText(dayPillEnd?.dropLast(9))
        edtName.setText(pillName)
        edtCount.setText(countPill.toString())

        // val count: Int = Integer.parseInt(edtCount.getText().toString());

        dateEdt_Start.setOnClickListener {

            val c = Calendar.getInstance()


            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    val dat: String
                    if (dayOfMonth <= 10 && monthOfYear > 10) {
                        dat =
                            (year.toString() + "-" + (monthOfYear + 1) + "-0" + dayOfMonth.toString())
                        dateEdt_Start.setText(dat)
                    } else if (dayOfMonth < 10 && monthOfYear < 10) {
                        dat =
                            (year.toString() + "-0" + (monthOfYear + 1) + "-0" + dayOfMonth.toString())
                        dateEdt_Start.setText(dat)
                    } else if (monthOfYear < 9 && dayOfMonth > 10) {
                        dat =
                            (year.toString() + "-0" + (monthOfYear + 1) + "-" + dayOfMonth.toString())
                        dateEdt_Start.setText(dat)
                    } else {
                        dat =
                            (year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString())
                        dateEdt_Start.setText(dat)
                    }
                },
                year,
                month,
                day
            )

            datePickerDialog.show()
        }

        dateEdt_End.setOnClickListener {

            val c = Calendar.getInstance()


            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    val dat: String
                    if (dayOfMonth <= 10 && monthOfYear > 10) {
                        dat =
                            (year.toString() + "-" + (monthOfYear + 1) + "-0" + dayOfMonth.toString())
                        dateEdt_End.setText(dat)
                    } else if (dayOfMonth < 10 && monthOfYear < 10) {
                        dat =
                            (year.toString() + "-0" + (monthOfYear + 1) + "-0" + dayOfMonth.toString())
                        dateEdt_End.setText(dat)
                    } else if (monthOfYear < 9 && dayOfMonth > 10) {
                        dat =
                            (year.toString() + "-0" + (monthOfYear + 1) + "-" + dayOfMonth.toString())
                        dateEdt_End.setText(dat)
                    } else {
                        dat =
                            (year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString())
                        dateEdt_End.setText(dat)
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

        edtCount.addTextChangedListener(object : TextWatcher {
            var editing = false
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!editing) {
                    editing = true
                    if (s?.toString()?.startsWith("0") == true) {
                        edtCount.setText(s.subSequence(1, s.length))
                        edtCount.setSelection(edtCount.text.length)
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

        val delItem = findViewById<Button>(R.id.DelButPill)
        delItem.setOnClickListener{
            retrofit.deletePills(idPill)
                .enqueue(object: Callback<DefaultResponse> {
                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, "Mistake. Try again!", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                        Toast.makeText(applicationContext, "Таблетка удалена!", Toast.LENGTH_SHORT).show()
                        val intent: Intent =
                            Intent(applicationContext, Pill::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                        finish()
                    }

                })
        }


        val updateItem = findViewById<Button>(R.id.ChangeButPill)
        updateItem.setOnClickListener{
            count = edtCount.getText().toString().trim().toInt();

            if (edtName.text.toString().equals(""))
            {
                edtName?.requestFocus()
                Toast.makeText(applicationContext, "Введите название лекарства!", Toast.LENGTH_SHORT).show()
            }
            else if (edtCount.text.toString().equals(""))
            {
                edtCount?.requestFocus()
                Toast.makeText(applicationContext, "Введите количество!", Toast.LENGTH_SHORT).show()
            }
            else if (edtCount.text.startsWith("0"))
            {
                edtCount?.requestFocus()
                Toast.makeText(applicationContext, "Количество таблеток не может быть отрицательным числом или равно 0!", Toast.LENGTH_SHORT).show()
            }
            else if (edtCount.text.toString().toInt() <= 0)
            {
                edtCount?.requestFocus()
                Toast.makeText(applicationContext, "Количество таблеток не может быть отрицательным числом или равно 0!", Toast.LENGTH_SHORT).show()
            }
            else if (txtTime.text.toString().equals(""))
            {
                txtTime.requestFocus()
                Toast.makeText(applicationContext, "Выберите время приёма!", Toast.LENGTH_SHORT).show()
            }
            else if (dateEdt_Start.text.toString().equals(""))
            {
                dateEdt_Start?.requestFocus()
                Toast.makeText(applicationContext, "Выберите дату начала!", Toast.LENGTH_SHORT).show()
            }
            else if (dateEdt_End.text.toString().equals(""))
            {
                dateEdt_End?.requestFocus()
                Toast.makeText(applicationContext, "Выберите дату окончания!", Toast.LENGTH_SHORT).show()
            }
            else {

            val obj = ChangePillModel(idPill,
                edtName.text.toString().trim(),
                count,
                "1900-01-01T" + txtTime.text.toString().trim(),
                dateEdt_Start.text.toString().trim(),
                dateEdt_End.text.toString().trim(),
                paperDbClass.getUser()!!.idLogPass,
                )

            retrofit.changePills(idPill, obj)
                .enqueue(object: Callback<DefaultResponse> {
                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, "Mistake. Try again!", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                        Toast.makeText(applicationContext, "Запись успешно изменена!", Toast.LENGTH_SHORT).show()
                        val intent: Intent =
                            Intent(applicationContext, Pill::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                        finish()
                    }

                })
        }}


    }
}